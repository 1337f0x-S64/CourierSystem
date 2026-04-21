package com.example.courier.utils.messages;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Message {

    public static void showError(String title, String content) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle(title);
        message.setHeaderText(null);
        message.setContentText(content);
        message.showAndWait();
    }

    public static void showInfo(String tittle, String content) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle(tittle);
        message.setHeaderText(null);
        message.setContentText(content);
        message.showAndWait();
    }

    public static void showWarning(String title, String content) {
        Alert message = new Alert(Alert.AlertType.WARNING);
        message.setTitle(title);
        message.setHeaderText(null);
        message.setContentText(content);
        message.showAndWait();
    }

    public static void showYesOrNot(String tittle, String content, Runnable actionYes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(content);
        alert.setContentText("Select an option:");

        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");

        alert.getButtonTypes().setAll(btnYes, btnNo);

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == btnYes) {
            actionYes.run();
        }
    }
    public static boolean showsConfirm(String title, String content) {
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setTitle(title);
        message.setHeaderText(null);
        message.setContentText(content);
        Optional<ButtonType> result = message.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
