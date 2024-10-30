package ubb.scs.map;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.memory.InMemoryRepository;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;
import ubb.scs.map.ui.Console;

public class Main {
    public static void main(String[] args) {

        Repository<Long, User> userRepository = new InMemoryRepository<>(new UserValidator());
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new InMemoryRepository<>(new FriendshipValidator());

        UserService userService = new UserService(userRepository, friendshipRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);

        Console console = new Console(userService, friendshipService);

        console.run();
    }
}