package ubb.scs.map.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    static void showInfoMessage(Stage owner, String text){
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.initOwner(owner);
        message.setTitle("Mesaj informativ");
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }
}

