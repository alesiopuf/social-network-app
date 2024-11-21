package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Status;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.dto.FriendshipDTO;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestsController {

    @FXML
    public TableView<FriendshipDTO> tableView;
    @FXML
    public TableColumn<FriendshipDTO, String> tableColumnTo;
    @FXML
    public TableColumn<FriendshipDTO, String> tableColumnDate;
    @FXML
    public TableColumn<FriendshipDTO, String> tableColumnStatus;
    Stage stage;
    UserService userService;
    FriendshipService friendshipService;
    Long userId;
    ObservableList<FriendshipDTO> model = FXCollections.observableArrayList();

    public void initWindow(Stage stage, UserService userService, FriendshipService friendshipService, Long userId) {
        this.stage = stage;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.userId = userId;
        initModel();
    }

    @FXML
    private void initialize() {
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<FriendshipDTO> friendships = friendshipService.getFriendRequests(userId);
        List<FriendshipDTO> friendshipsList = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendshipsList);
    }
}
