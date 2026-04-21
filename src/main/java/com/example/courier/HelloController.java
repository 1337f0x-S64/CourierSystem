package com.example.courier;

import com.example.courier.domain.services.AuthenticationService;
import com.example.courier.infrastructure.repositories.UserRepositoryImpl;
import com.example.courier.utils.windows.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class HelloController {

    // DDD: AuthenticationService is the entry point
    private final AuthenticationService authService =
            new AuthenticationService(new UserRepositoryImpl());

    @FXML private Label         welcomeText;
    @FXML private TextField     txtUser;
    @FXML private PasswordField txtPassword;
    @FXML private ImageView     imgLogo;

    @FXML
    public void initialize() {
        Image imageLogo = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/com/example/courier/images/Logot.png")));
        imgLogo.setImage(imageLogo);
    }

    @FXML
    protected void onHelloButtonClick() {
        String username = txtUser.getText();
        String password = txtPassword.getText();
        welcomeText.setText("Session started");

        // DDD: delegate authentication to the domain service — encryption happens inside
        if (authService.authenticate(username, password)) {
            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);
            loginAlert.setTitle("Login");
            loginAlert.setHeaderText(null);
            loginAlert.setContentText("Successful login");
            Stage stage = (Stage) loginAlert.getDialogPane().getScene().getWindow();
            stage.setMinWidth(400);
            stage.setMinHeight(200);
            loginAlert.showAndWait();
            openMainMenu();
        } else {
            Alert loginAlert = new Alert(Alert.AlertType.ERROR);
            loginAlert.setTitle("Login");
            loginAlert.setHeaderText(null);
            loginAlert.setContentText("Login failed");
            Stage stage = (Stage) loginAlert.getDialogPane().getScene().getWindow();
            stage.setMinWidth(400);
            stage.setMinHeight(200);
            loginAlert.showAndWait();
        }
    }

    private void openMainMenu() {
        Windows.newWindow("/com/example/courier/views/MainMenu.fxml",
                "Main Menu Courier Management System", 400, 650,
                Optional.of(false), "tracking.png");
        ((Stage) txtUser.getScene().getWindow()).close();
    }
}