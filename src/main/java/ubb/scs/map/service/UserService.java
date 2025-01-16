package ubb.scs.map.service;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.exception.UserAlreadyExistsException;
import ubb.scs.map.domain.exception.UserNotFoundException;
import ubb.scs.map.repository.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class UserService {

    private final Repository<Long, User> userRepository;
    private final Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    public UserService(Repository<Long, User> userRepository, Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public User addUser(Long id, String firstName, String lastName) {
        User user = new User(firstName, lastName, firstName+ lastName, Objects.hash(firstName + lastName));
        user.setId(id);
        userRepository.save(user).ifPresent(u -> {
            throw new UserAlreadyExistsException(u.toString());
        });
        return user;
    }

    public User deleteUser(Long id) {
        User user = userRepository.delete(id).orElseThrow(() -> new UserNotFoundException(id));
        List<Friendship> toBeDeleted = StreamSupport.stream(friendshipRepository.findAll().spliterator(), false)
                .filter(f -> f.getFirst().equals(id) || f.getSecond().equals(id)).toList();
        toBeDeleted.forEach(f -> friendshipRepository.delete(f.getId()));
        return user;
    }

    public User getUserByUsername(String username) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
