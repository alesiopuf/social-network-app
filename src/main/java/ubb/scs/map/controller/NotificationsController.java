package ubb.scs.map.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Status;
import ubb.scs.map.domain.User;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NotificationsController {

    @FXML
    TableView<Friendship> tableViewNotification;

    @FXML
    TableColumn<Friendship, String> tableViewNotificationColumnFirstName;
    @FXML
    TableColumn<Friendship, String> tableViewNotificationColumnLastName;

    @FXML
    TableColumn<Friendship, String> tableViewNotificationColumnStatus;
    Stage primaryStage;
    UserService service;
    FriendshipService friendService;
    User user;
    ObservableList<Friendship> modelNotification = FXCollections.observableArrayList();

    public void initWindow(UserService service, FriendshipService friendService, User user, Stage stage) {
        this.service = service;
        this.friendService = friendService;
        this.user = user;
        this.primaryStage = stage;
        initModelNotification();
    }

    public void initialize() {
        tableViewNotificationColumnFirstName.setCellValueFactory(cellData -> {
            Friendship friendship = cellData.getValue();
            Long id = friendship.getFirst();
            String userName = service.getUserById(id).getFirstName();
            return new SimpleStringProperty(userName);
        });
        tableViewNotificationColumnLastName.setCellValueFactory(cellData -> {
            Friendship friendship = cellData.getValue();
            Long id = friendship.getFirst();
            String userName = service.getUserById(id).getLastName();
            return new SimpleStringProperty(userName);
        });
        tableViewNotificationColumnStatus.setCellValueFactory(cellData -> {
            Friendship friendship = cellData.getValue();
            String date = friendship.getStatus().toString();
            return new SimpleStringProperty(date);
        });
        tableViewNotification.setItems(modelNotification);

    }

    private void initModelNotification() {
        Iterable<Friendship> messages = friendService.getAll();
        List<Friendship> friends = StreamSupport.stream(messages.spliterator(), false)
                .filter(u -> (u.getSecond().equals(user.getId())))
                .filter(u -> (u.getStatus() == Status.PENDING))
                .collect(Collectors.toList());
        friends.forEach(System.out::println);
        modelNotification.setAll(friends);
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        Friendship friend = (Friendship) tableViewNotification.getSelectionModel().getSelectedItem();
        if (friend != null) {
            Friendship fr = friendService.deleteFriendship(friend.getId().getFirst(), friend.getId().getSecond());
        }
        initModelNotification();
    }

    public void handleAcceptFriendship(ActionEvent actionEvent) {
        Friendship friend = (Friendship) tableViewNotification.getSelectionModel().getSelectedItem();
        if (friend != null) {
            if (friend.getStatus() == Status.PENDING && friend.getSecond().equals(user.getId())) {
                Friendship fr = friendService.setStatus(friend.getId(), Status.ACCEPTED);
            } else {
                MessageAlert.showErrorMessage(primaryStage, "Friendship was already handled");
            }
        }
        initModelNotification();
    }
}
