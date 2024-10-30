package ubb.scs.map.service;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.exception.FriendshipAlreadyExistsException;
import ubb.scs.map.domain.exception.FriendshipNotFoundException;
import ubb.scs.map.domain.exception.UserNotFoundException;
import ubb.scs.map.repository.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService {

    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;

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
        return friendshipRepository.delete(id).orElseThrow(() -> new FriendshipNotFoundException(id));
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
}
