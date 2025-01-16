package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.dto.PageDTO;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

public class ProfilePageController {

    @FXML
    public Label usernameText;
    @FXML
    public Label friendsNumberLabel;
    @FXML
    public TextArea descriptionText;
    Stage primaryStage;
    UserService service;
    FriendshipService friendService;
    User user;

    public void initWindow(UserService service, FriendshipService friendService, User user, Stage stage) {
        this.service = service;
        this.friendService = friendService;
        this.user = user;
        this.primaryStage = stage;
        initializeComponents();
    }

    private void initializeComponents() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setUsername(user.getUsername());
        pageDTO.setNumberOfFriends(friendService.getFriends(user.getId()).size());
        pageDTO.setDescription("Hello, my name is " + user.getFirstName());

        usernameText.setText(pageDTO.getUsername());
        descriptionText.setText(pageDTO.getDescription());
        friendsNumberLabel.setText(String.valueOf(pageDTO.getNumberOfFriends()));
    }
}
