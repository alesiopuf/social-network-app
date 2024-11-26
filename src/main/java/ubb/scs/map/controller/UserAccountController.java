package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ubb.scs.map.HelloApplication;
import ubb.scs.map.domain.User;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.MessageService;
import ubb.scs.map.service.UserService;
import ubb.scs.map.util.events.UserEntityChangeEvent;
import ubb.scs.map.util.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserAccountController implements Observer<UserEntityChangeEvent> {

    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, String> tableColumnUsername;
    @FXML
    public TableColumn<User, String> tableColumnFirstname;
    @FXML
    public TableColumn<User, String> tableColumnLastname;
    Stage stage;
    UserService userService;
    FriendshipService friendshipService;
    MessageService messageService;
    Long userId;
    ObservableList<User> model = FXCollections.observableArrayList();

    public void initWindow(Stage stage, UserService userService, FriendshipService friendshipService, MessageService messageService, Long userId) {
        this.stage = stage;
        this.userService = userService;
        this.friendshipService = friendshipService;
        friendshipService.addObserver(this);
        this.messageService = messageService;
        this.userId = userId;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> users = friendshipService.getFriends(userId);
        List<User> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }

    public void handleRemoveFriend(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            friendshipService.deleteFriendship(userId, user.getId());
        }
    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/add_friend.fxml"));
        Stage windowStage = new Stage();

        AnchorPane userLayout = fxmlLoader.load();
        windowStage.setScene(new Scene(userLayout));

        AddFriendController addFriendController = fxmlLoader.getController();
        addFriendController.initWindow(windowStage, userService, friendshipService, userId);

        windowStage.setTitle("Add friend menu");
        windowStage.show();
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }

    public void handleShowFriendRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/friend_requests.fxml"));
        Stage windowStage = new Stage();

        AnchorPane userLayout = fxmlLoader.load();
        windowStage.setScene(new Scene(userLayout));

        FriendRequestsController friendRequestsController = fxmlLoader.getController();
        friendRequestsController.initWindow(windowStage, userService, friendshipService, userId);

        windowStage.setTitle("Add friend menu");
        windowStage.show();
    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));

        GridPane userLayout = fxmlLoader.load();
        stage.setScene(new Scene(userLayout));

        LoginController loginController = fxmlLoader.getController();
        loginController.initWindow(stage, userService, friendshipService, messageService);
        stage.setTitle("Login");
    }

    public void handleChat(ActionEvent actionEvent) throws IOException {
        User user = tableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            MessageAlert.showErrorMessage(stage, "Please select a friend to chat with!");
            return;
        }
        Long recipientId = user.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/chat.fxml"));
        Stage windowStage = new Stage();

        AnchorPane userLayout = fxmlLoader.load();
        windowStage.setScene(new Scene(userLayout));

        ChatController chatController = fxmlLoader.getController();
        chatController.initWindow(stage, messageService, userService, userId, recipientId);

        windowStage.setTitle("Chat");
        windowStage.show();
    }

    public void handleNotifications(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/notifications.fxml"));
        Stage windowStage = new Stage();

        AnchorPane userLayout = fxmlLoader.load();
        windowStage.setScene(new Scene(userLayout));

        NotificationsController notificationsController = fxmlLoader.getController();
        notificationsController.initWindow(userService, friendshipService, userService.getUserById(userId),windowStage);

        windowStage.setTitle("Notifications");
        windowStage.show();
    }
}
