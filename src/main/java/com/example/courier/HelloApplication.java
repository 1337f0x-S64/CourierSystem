package com.example.courier;

import com.example.courier.utils.windows.Windows;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Windows.newWindow("/com/example/courier/hello-view.fxml", "Login Courier Management System", 520, 440, Optional.of(false), "login.png");
    }
    public static void main(String[] args) {
        launch();
    }
}