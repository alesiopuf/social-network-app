package ubb.scs.map.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ubb.scs.map.domain.Message;
import ubb.scs.map.service.MessageService;
import ubb.scs.map.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController {
    @FXML
    public TextField messageTextField;
    @FXML
    public ListView<String> chatListView;
    @FXML
    public Label chatLabel;
    Stage stage;
    MessageService messageService;
    UserService userService;
    Long userId;
    Long recipientId;
    ObservableList<String> model = FXCollections.observableArrayList();

    public void initWindow(Stage stage, MessageService messageService, UserService userService, Long userId, Long recipientId) {
        this.stage = stage;
        this.messageService = messageService;
        this.userService = userService;
        this.userId = userId;
        this.recipientId = recipientId;
        chatLabel.setText("Chat with " + userService.getUserById(recipientId).getUsername());
        initModel();
    }

    private void initModel() {
        Iterable<Message> users = messageService.getMessagesForUsers(userId, recipientId);
        List<String> processedMessageList = StreamSupport.stream(users.spliterator(), false)
                .map(message -> message.getDate() + " " + userService.getUserById(message.getFrom()).getUsername() + ": " + message.getMessage())
                .collect(Collectors.toList());
        model.setAll(processedMessageList);
    }

    @FXML
    public void initialize() {
        chatListView.setItems(model);
    }

    public void handleSend(ActionEvent actionEvent) {
        String message = messageTextField.getText();
        messageService.addMessage(userId, Arrays.asList(recipientId), message);
        messageTextField.clear();
        initModel();
    }
}
