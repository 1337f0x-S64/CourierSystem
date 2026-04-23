package com.example.courier.controllers;

import static com.example.courier.controllers.UsersViewsController.refreshData;
import com.example.courier.controllers.interfaces.ConfigurableController;
import com.example.courier.db.RoleDatabase;
import com.example.courier.domain.repositories.UserRepository;
import com.example.courier.domain.repositories.UserRepository.UserSummary;
import com.example.courier.infrastructure.repositories.UserRepositoryImpl;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.security.EncryptPassword;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;


public class AddEditUserController implements ConfigurableController {


    private final UserRepository userRepository = new UserRepositoryImpl();

    @FXML private TextField        txtId;
    @FXML private TextField        txtName;
    @FXML private TextField        txtLastName;
    @FXML private TextField        txtUserName;
    @FXML private TextField        txtEmail;
    @FXML private PasswordField    txtPassword;
    @FXML private ComboBox<String> cmbxRole;
    @FXML private Button           btnSave;

    @Override
    public void setParameter(Object params) {
        if (params instanceof Integer userId) {
            loadUser(userId);
        }
    }

    public void initialize() {
        txtName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtLastName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtUserName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(50)));
        txtEmail.setTextFormatter(new TextFormatter<>(TextFormat.textLength(100)));

        // Populate roles — no DB call needed, these are fixed domain values
        cmbxRole.getItems().addAll("Customer", "Courier", "Administrator");

        btnSave.setOnAction(e -> saveUser());
    }


    private void loadUser(int userId) {
        userRepository.findById(userId).ifPresentOrElse(
                this::populateFields,
                () -> Message.showError("Error", "User not found.")
        );
    }

    private void populateFields(UserSummary user) {
        txtId.setText(String.valueOf(user.id()));
        txtName.setText(user.name());
        txtLastName.setText(user.lastname());
        txtUserName.setText(user.username());
        txtEmail.setText(user.email());

        String roleDisplay = switch (user.roleName().trim().toLowerCase()) {
            case "administrator", "admin" -> "Administrator";
            case "courier"               -> "Courier";
            default                      -> "Customer";
        };
        cmbxRole.setValue(roleDisplay);
    }

    private void saveUser() {
        if (!validateInputs()) return;

        String selectedRole      = cmbxRole.getSelectionModel().getSelectedItem();
        int    roleId            = RoleDatabase.getIdByName(selectedRole);
        String encryptedPassword = EncryptPassword.encryptSHA256(txtPassword.getText());

        boolean success;
        String  successMessage;

        if (txtId.getText().isEmpty()) {
            success = userRepository.save(
                    txtName.getText(),
                    txtLastName.getText(),
                    txtEmail.getText(),
                    txtUserName.getText(),
                    encryptedPassword,
                    roleId
            );
            successMessage = "User created successfully.";
        } else {
            success = userRepository.update(
                    Integer.parseInt(txtId.getText()),
                    txtName.getText(),
                    txtLastName.getText(),
                    txtEmail.getText(),
                    txtUserName.getText(),
                    encryptedPassword,
                    roleId
            );
            successMessage = "User updated successfully.";
        }

        if (success) {
            Message.showInfo("Success", successMessage);
            refreshData();
            ((Stage) txtUserName.getScene().getWindow()).close();
        } else {
            Message.showError("Error", "An error occurred while saving the user.");
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().isEmpty()) {
            Message.showError("User Error", "Name is required.");
            return false;
        }
        if (!txtName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("User Error", "Name must contain only letters.");
            return false;
        }
        if (txtLastName.getText().isEmpty()) {
            Message.showError("User Error", "Last name is required.");
            return false;
        }
        if (!txtLastName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("User Error", "Last name must contain only letters.");
            return false;
        }
        if (txtUserName.getText().isEmpty()) {
            Message.showError("User Error", "Username is required.");
            return false;
        }
        if (txtEmail.getText().isEmpty()) {
            Message.showError("User Error", "Email is required.");
            return false;
        }
        if (!txtEmail.getText()
                .matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            Message.showError("User Error", "Invalid email format.");
            return false;
        }
        if (txtPassword.getText().isEmpty()) {
            Message.showError("User Error", "Password is required.");
            return false;
        }
        if (cmbxRole.getSelectionModel().getSelectedItem() == null) {
            Message.showError("User Error", "Role selection is required.");
            return false;
        }
        return true;
    }
}