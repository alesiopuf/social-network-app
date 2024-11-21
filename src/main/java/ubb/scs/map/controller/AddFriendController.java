package ubb.scs.map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

public class AddFriendController {

    Stage stage;
    UserService userService;
    FriendshipService friendshipService;
    Long userId;
    @FXML
    public TextField textFieldUsername;

    public void initWindow(Stage stage, UserService userService, FriendshipService friendshipService, Long userId) {
        this.stage = stage;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.userId = userId;
    }

    @FXML
    private void initialize() {
    }

    public void handleAddFriend(ActionEvent actionEvent) {
        try {
            String username = textFieldUsername.getText();
            Long friendId = userService.getUserByUsername(username).getId();
            friendshipService.addFriendship(userId, friendId);
            MessageAlert.showInfoMessage(stage, "Friend \"" + username + "\" added!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }
}
