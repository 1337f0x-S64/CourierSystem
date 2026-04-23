package com.example.courier.controllers;

import com.example.courier.db.MyUserViewsDatabase;
import com.example.courier.models.UserGlobal;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.security.EncryptPassword;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class MyUserViewsController {

    @FXML private TextField     txtName;
    @FXML private TextField     txtLastName;
    @FXML private TextField     txtUserName;
    @FXML private TextField     txtEmail;
    @FXML private Button        btnUpdate;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;

    public void initialize() {
        txtName.setText(UserGlobal.getInstance().getName());
        txtLastName.setText(UserGlobal.getInstance().getLastName());
        txtUserName.setText(UserGlobal.getInstance().getUsername());
        txtEmail.setText(UserGlobal.getInstance().getEmail());

        txtName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtLastName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtUserName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtEmail.setTextFormatter(new TextFormatter<>(TextFormat.textLength(100)));

        btnUpdate.setOnAction(e -> updateMyUser());
    }

    private void updateMyUser() {


        if (txtName.getText().isEmpty()) {                         
            Message.showError("Update Error", "Name is required.");
            return;
        }
        if (!txtName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("Update Error", "Name must contain only letters.");
            return;
        }
        if (txtLastName.getText().isEmpty()) {                       
            Message.showError("Update Error", "Last name is required.");
            return;
        }
        if (!txtLastName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("Update Error", "Last name must contain only letters.");
            return;
        }
        if (txtUserName.getText().isEmpty()) {                       
            Message.showError("Update Error", "Username is required.");
            return;
        }
        if (txtEmail.getText().isEmpty()) {                          
            Message.showError("Update Error", "Email is required.");
            return;
        }



        boolean newPasswordFilled     = !txtNewPassword.getText().isEmpty();
        boolean confirmPasswordFilled = !txtConfirmPassword.getText().isEmpty();

        String password;

        if (newPasswordFilled && confirmPasswordFilled) {

            String storedEncrypted  = UserGlobal.getInstance().getPassword();
            String enteredEncrypted = EncryptPassword.encryptSHA256(txtPassword.getText());

            if (!storedEncrypted.equals(enteredEncrypted)) {
                Message.showError("Update Error", "Incorrect current password.");
                return;
            }
            
            if (!txtNewPassword.getText().equals(txtConfirmPassword.getText())) {
                Message.showError("Update Error", "Passwords do not match.");
                return;
            }
            password = EncryptPassword.encryptSHA256(txtNewPassword.getText());

        } else if (newPasswordFilled || confirmPasswordFilled) {
            
            Message.showError("Update Error",
                    "Both new password and confirmation must be filled.");
            return;

        } else {
            
            password = UserGlobal.getInstance().getPassword();
        }


        MyUserViewsDatabase.updateMyUser(
                UserGlobal.getInstance().getId(),
                txtName.getText(),
                txtLastName.getText(),
                txtUserName.getText(),
                txtEmail.getText(),
                password
        );

        Message.showInfo("Update Successful",
                "Your profile has been updated successfully.");
    }
}