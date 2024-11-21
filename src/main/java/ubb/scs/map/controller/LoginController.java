package ubb.scs.map.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ubb.scs.map.HelloApplication;
import ubb.scs.map.domain.User;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

public class LoginController {

    @FXML
    public TextField textFieldUsername;
    @FXML
    public PasswordField passwordField;
    Stage stage;
    UserService userService;
    FriendshipService friendshipService;

    public void initWindow(Stage stage, UserService userService, FriendshipService friendshipService) {
        this.stage = stage;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @FXML
    private void initialize() {
    }

    public void handleLogin() {
        try {
            User user = userService.getUserByUsername(textFieldUsername.getText());
            if (!passwordField.getText().equals(user.getPassword())) {
                throw new Exception("Password is not correct!");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/user_account.fxml"));
            Stage windowStage = new Stage();

            AnchorPane userLayout = fxmlLoader.load();
            windowStage.setScene(new Scene(userLayout));

            UserAccountController userAccountController = fxmlLoader.getController();
            userAccountController.initWindow(windowStage, userService,friendshipService, user.getId());

            windowStage.setTitle("Account for " + user.getUsername());
            windowStage.show();
            stage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }
}
