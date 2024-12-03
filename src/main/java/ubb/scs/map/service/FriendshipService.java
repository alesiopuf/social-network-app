package ubb.scs.map.service;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Status;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.dto.FriendshipDTO;
import ubb.scs.map.domain.exception.FriendshipAlreadyExistsException;
import ubb.scs.map.domain.exception.FriendshipNotFoundException;
import ubb.scs.map.domain.exception.UserNotFoundException;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.util.events.ChangeEventType;
import ubb.scs.map.util.events.UserEntityChangeEvent;
import ubb.scs.map.util.observer.Observable;
import ubb.scs.map.util.observer.Observer;
import ubb.scs.map.util.paging.Page;
import ubb.scs.map.util.paging.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService implements Observable<UserEntityChangeEvent> {

    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;

    private List<Observer<UserEntityChangeEvent>> observers=new ArrayList<>();

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository, Repository<Long, User> userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public Friendship addFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.findOne(userId1);
        Optional<User> user2 = userRepository.findOne(userId2);
        if (user1.isEmpty()) {
            throw new UserNotFoundException(userId1);
        }
        if (user2.isEmpty()) {
            throw new UserNotFoundException(userId2);
        }
        Friendship friendship = new Friendship(new Tuple<>(userId1, userId2));
        friendshipRepository.save(friendship).ifPresent(f -> {
            throw new FriendshipAlreadyExistsException(f.toString());
        });
        notifyObservers(new UserEntityChangeEvent(ChangeEventType.ADD, friendship));
        return friendship;
    }

    public Friendship deleteFriendship(Long userId1, Long userId2) {
        Optional<User> user1 = userRepository.findOne(userId1);
        Optional<User> user2 = userRepository.findOne(userId2);
        if (user1.isEmpty()) {
            throw new UserNotFoundException(userId1);
        }
        if (user2.isEmpty()) {
            throw new UserNotFoundException(userId2);
        }
        Tuple<Long, Long> id = new Tuple<>(userId1, userId2);
        Friendship friendship;
        try {
            Tuple<Long, Long> finalId = id;
            friendship = friendshipRepository.delete(id).orElseThrow(() -> new FriendshipNotFoundException(finalId));
        } catch (FriendshipNotFoundException e) {
            id = new Tuple<>(userId2, userId1);
            Tuple<Long, Long> finalId1 = id;
            friendship = friendshipRepository.delete(id).orElseThrow(() -> new FriendshipNotFoundException(finalId1));
        }
        notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, friendship));
        return friendship;
    }

    public List<User> getFriends(Long userId) {
        Optional<User> user = userRepository.findOne(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        return StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> friendship.getFirst().equals(userId) || friendship.getSecond().equals(userId))
                .filter(friendship -> friendship.getStatus().equals(Status.ACCEPTED))
                .map(friendship -> {
                    Long friendId = friendship.getFirst().equals(userId) ? friendship.getSecond() : friendship.getFirst();
                    return userRepository.findOne(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
                })
                .toList();
    }

    public Page<User> getFriendsOnPage(Pageable pageable, Long userId) {
        Optional<User> user = userRepository.findOne(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        List<User> allFriends = StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(friendship -> friendship.getFirst().equals(userId) || friendship.getSecond().equals(userId))
                .filter(friendship -> friendship.getStatus().equals(Status.ACCEPTED))
                .map(friendship -> {
                    Long friendId = friendship.getFirst().equals(userId) ? friendship.getSecond() : friendship.getFirst();
                    return userRepository.findOne(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
                })
                .toList();

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int startIndex = pageSize * pageNumber;
        int endIndex = Math.min(startIndex + pageSize, allFriends.size());

        if (startIndex >= allFriends.size()) {
            return new Page<>(List.of(), allFriends.size());
        }

        List<User> paginatedFriends = allFriends.subList(startIndex, endIndex);
        return new Page<>(paginatedFriends, allFriends.size());
    }


    public List<FriendshipDTO> getFriendRequests(Long userId) {
        Optional<User> user = userRepository.findOne(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        return StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> friendship.getFirst().equals(userId))
                .map(friendship -> {
                    Long friendId = friendship.getFirst().equals(userId) ? friendship.getSecond() : friendship.getFirst();
                    User friend = userRepository.findOne(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
                    return new FriendshipDTO(friend.getUsername(), friendship.getDate().toString(), friendship.getStatus().toString());
                })
                .toList();
    }

    public List<Friendship> getAll() {
        return StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Friendship setStatus(Tuple<Long, Long> id, Status newStatus) {
        Friendship friendship = friendshipRepository.findOne(id).orElseThrow(() -> new FriendshipNotFoundException(id));
        friendship.setStatus(newStatus);
        friendshipRepository.delete(friendship.getId());
        friendshipRepository.save(friendship);
        notifyObservers(new UserEntityChangeEvent(ChangeEventType.UPDATE, friendship));
        return friendship;
    }

    public int getNumberOfCommunities() {
        Iterable<User> users = userRepository.findAll();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        Map<Long, List<Long>> adjacencyList = buildAdjacencyList(users, friendships);
        Set<Long> visited = new HashSet<>();
        int numberOfCommunities = 0;

        for (Long user : adjacencyList.keySet()) {
            if (!visited.contains(user)) {
                numberOfCommunities++;
                dfs(user, adjacencyList, visited, new ArrayList<>());
            }
        }

        return numberOfCommunities;
    }

    public List<Long> getMostSociableCommunity() {
        Iterable<User> users = userRepository.findAll();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        Map<Long, List<Long>> adjacencyList = buildAdjacencyList(users, friendships);
        Set<Long> visited = new HashSet<>();
        List<Long> mostSociableCommunity = new ArrayList<>();
        int maxCommunitySize = 0;

        for (Long user : adjacencyList.keySet()) {
            if (!visited.contains(user)) {
                List<Long> community = new ArrayList<>();
                dfs(user, adjacencyList, visited, community);
                if (community.size() > maxCommunitySize) {
                    maxCommunitySize = community.size();
                    mostSociableCommunity = community;
                }
            }
        }

        return mostSociableCommunity;
    }

    private Map<Long, List<Long>> buildAdjacencyList(Iterable<User> users, Iterable<Friendship> friendships) {
        Map<Long, List<Long>> adjacencyList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toMap(User::getId, user -> new ArrayList<>()));

        friendships.forEach(friendship -> {
            Long user1 = friendship.getFirst();
            Long user2 = friendship.getSecond();
            adjacencyList.computeIfAbsent(user1, k -> new ArrayList<>()).add(user2);
            adjacencyList.computeIfAbsent(user2, k -> new ArrayList<>()).add(user1);
        });

        return adjacencyList;
    }

    private void dfs(Long user, Map<Long, List<Long>> adjacencyList, Set<Long> visited, List<Long> community) {
        Stack<Long> stack = new Stack<>();
        stack.push(user);
        while (!stack.isEmpty()) {
            Long current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                community.add(current);
                adjacencyList.get(current).stream()
                        .filter(neighbor -> !visited.contains(neighbor))
                        .forEach(stack::push);
            }
        }
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }
}
