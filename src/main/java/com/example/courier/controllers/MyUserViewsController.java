package com.example.courier.controllers;

import com.example.courier.db.MyUserViewsDatabase;
import com.example.courier.models.UserGlobal;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.security.EncryptPassword;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MyUserViewsController {
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnUpdate;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private  PasswordField txtNewPassword;

    @FXML
    private  PasswordField txtConfirmPassword;

    public void initialize() {
        txtName.setText(UserGlobal.getInstance().getName());
        txtLastName.setText(UserGlobal.getInstance().getLastName());
        txtUserName.setText(UserGlobal.getInstance().getUsername());
        txtEmail.setText(UserGlobal.getInstance().getEmail());

        txtName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtLastName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtUserName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtEmail.setTextFormatter(new TextFormatter<>(TextFormat.textLength(30)));

        btnUpdate.setOnAction(e -> updateMyUser());
    }

    private void updateMyUser() {
        if (txtName.getText() == "") {
            Message.showError("Update Error", "Name is required.");
            return;
        }
        if (!txtName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("Update Error", "Name must contain only letters.");
            return;
        }
        if (txtLastName.getText() == "") {
            Message.showError("Update Error", "Last name is required.");
            return;
        }
        if (!txtLastName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("Update Error", "Last name must contain only letters.");
            return;
        }
        if (txtUserName.getText() == "") {
            Message.showError("Update Error", "Username is required.");
            return;
        }
        if (txtEmail.getText() == "") {
            Message.showError("Update Error", "Email is required.");
            return;
        }
        String password = null;

        if (!txtNewPassword.getText().isEmpty() && !txtConfirmPassword.getText().isEmpty()) {

            if (!UserGlobal.getInstance().getPassword().equals(EncryptPassword.encryptSHA256(txtPassword.getText()))) {
                Message.showError("Update Error", "Incorrect current password.");
                return;
            }
            if (txtNewPassword.getText().equals(txtConfirmPassword.getText())) {
                password = EncryptPassword.encryptSHA256(txtNewPassword.getText());
            } else {
                Message.showError("Update Error", "Passwords do not match.");
                return;
            }

        } else if (!txtNewPassword.getText().isEmpty() && txtConfirmPassword.getText().isEmpty() || txtNewPassword.getText().isEmpty() && !txtConfirmPassword.getText().isEmpty()) {
            Message.showError("Update Error", "Both new password and confirmation must be filled." );
            return;
        } else {
            password = UserGlobal.getInstance().getPassword();
        }
        MyUserViewsDatabase.updateMyUser(UserGlobal.getInstance().getId(), txtName.getText(), txtLastName.getText(), txtUserName.getText(), txtEmail.getText(), password);
        Message.showInfo("Update Successful", "Your profile has been updated successfully.");
    }
}

