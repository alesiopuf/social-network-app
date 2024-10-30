package ubb.scs.map.ui;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.User;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

import java.util.Scanner;

public class Console {
    private UserService userService;

    private FriendshipService friendshipService;
    private Scanner scanner;

    public Console(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            try {
                char option = scanner.nextLine().charAt(0);
                switch (option) {
                    case '1' -> handleAddUser();
                    case '2' -> handleRemoveUser();
                    case '3' -> handleAddFriendship();
                    case '4' -> handleRemoveFriendship();
                    case '5' -> handlePrintNumberOfCommunities();
                    case '6' -> handlePrintMostSociableCommunity();
                    case 'q' -> exit = true;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }

    private void handleAddUser() {
        System.out.println("Give the user's id: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println("Give the user's name (separated by space): ");
        String name = scanner.nextLine();
        String[] names = name.split(" ");
        User user = userService.addUser(id, names[0], names[1]);
        System.out.println("User added: " + user);
    }

    private void handleRemoveUser() {
        System.out.println("Give the user's id: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userService.deleteUser(id);
        System.out.println("User removed: " + user);
    }

    private void handleAddFriendship() {
        System.out.println("Give the first user's id: ");
        Long id1 = Long.parseLong(scanner.nextLine());
        System.out.println("Give the second user's id: ");
        Long id2 = Long.parseLong(scanner.nextLine());
        Friendship friendship = friendshipService.addFriendship(id1, id2);
        System.out.println("Friendship added: " + friendship);
    }

    private void handleRemoveFriendship() {
        System.out.println("Give the first user's id: ");
        Long id1 = Long.parseLong(scanner.nextLine());
        System.out.println("Give the second user's id: ");
        Long id2 = Long.parseLong(scanner.nextLine());
        Friendship friendship = friendshipService.deleteFriendship(id1, id2);
        System.out.println("Friendship removed: " + friendship);
    }

    private void handlePrintNumberOfCommunities() {
        System.out.println("Number of communities: " + friendshipService.getNumberOfCommunities());
    }

    private void handlePrintMostSociableCommunity() {
        System.out.println("Most sociable community: " + friendshipService.getMostSociableCommunity());
    }

    private static void printMenu() {
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. Print number of communities");
        System.out.println("6. Print the most sociable community");
        System.out.println("q. Quit");
    }
}