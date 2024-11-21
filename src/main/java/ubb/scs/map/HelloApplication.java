package ubb.scs.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ubb.scs.map.controller.LoginController;
import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.FriendshipValidator;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.FriendshipDatabaseRepository;
import ubb.scs.map.repository.database.UserDatabaseRepository;
import ubb.scs.map.service.FriendshipService;
import ubb.scs.map.service.UserService;

import java.io.IOException;

public class HelloApplication extends Application {
    UserService userService;
    FriendshipService friendshipService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Repository<Long, User> userRepository = new UserDatabaseRepository(new UserValidator());
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDatabaseRepository(new FriendshipValidator());

        userService = new UserService(userRepository, friendshipRepository);
        friendshipService = new FriendshipService(friendshipRepository, userRepository);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));

        GridPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LoginController loginController = fxmlLoader.getController();
        loginController.initWindow(primaryStage, userService, friendshipService);
    }
}
