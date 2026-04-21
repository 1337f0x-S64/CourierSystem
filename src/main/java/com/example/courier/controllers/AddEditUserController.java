package com.example.courier.controllers;
import com.example.courier.controllers.interfaces.ConfigurableController;
import com.example.courier.db.RoleDatabase;
import com.example.courier.db.UsersDatabase;
import com.example.courier.models.Users;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.security.EncryptPassword;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import static com.example.courier.controllers.UsersViewsController.refreshData;

public class AddEditUserController implements ConfigurableController {
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML ComboBox cmbxRole;

    @FXML
    private Button btnSave;

    @Override
    public void setParameter(Object params) {
        if (params instanceof Integer) {
            int userId = (int) params;
            loadUser(userId);
        }
    }
    public void initialize() {
        txtName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtLastName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtUserName.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtEmail.setTextFormatter(new TextFormatter<>(TextFormat.textLength(30)));

        List<String> roles = RoleDatabase.loadRoles();

        cmbxRole.getItems().addAll("Customer", "Courier", "Administrator");

        btnSave.setOnAction(e -> saveUser());
    }

    private void loadUser (int userId) {
        Users user = UsersDatabase.loadUserId(userId);

        txtId.setText(String.valueOf(user.getId()));
        txtName.setText(user.getName());
        txtLastName.setText(user.getLastName());
        txtUserName.setText(user.getUserName());
        txtEmail.setText(user.getEmail());
        String val = RoleDatabase.getIdById(user.getRole());
        cmbxRole.setValue(val);
    }
    private void saveUser() {
        if (txtName.getText().isEmpty()) {
            Message.showError("User Error", "Name is required. ");
            return;
        }
        if (!txtName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("User Error", "Name must contain only letters.");
            return;
        }

        if (txtLastName.getText().isEmpty()){
            Message.showError("User Error", "Last name is required.");
            return;
        }
        if (!txtLastName.getText().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            Message.showError("User Error", "Last name must contain only letters.");
            return;
        }
        if (txtUserName.getText().isEmpty()) {
            Message.showError("User Error", "Username is required.");
            return;
        }
        if (txtEmail.getText().isEmpty()) {
            Message.showError("User Error", "Email is required.");
            return;
        } else {
            String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            boolean emailValid = txtEmail.getText().matches(regex);
            if (!emailValid) {
                Message.showError("User Error", "Invalid email format");
                return;
            }
        }
        if (txtPassword.getText().isEmpty()) {
            Message.showError("User Error", "Password is required.");
            return;
        }
        String selectedRole = (String) cmbxRole.getSelectionModel().getSelectedItem();
        if (selectedRole == null) {
            Message.showError("User Error", "Role selection is required.");
            return;
        }

        if (txtId.getText().isEmpty()) {
            int role = RoleDatabase.getIdByName(selectedRole);
            Boolean success = UsersDatabase.createUser(txtName.getText(), txtLastName.getText(), txtEmail.getText(),
                    txtUserName.getText(), EncryptPassword.encryptSHA256(txtPassword.getText()), role);

            if (success) {
                Message.showInfo("Success", "User updated successfully.");
                refreshData();
                Stage mainStage = (Stage) txtUserName.getScene().getWindow();
                mainStage.close();
            } else {
                Message.showError("Error", "An error occurred while updating the user.");
            }
        } else {
            int role = RoleDatabase.getIdByName(selectedRole);
            Boolean success = UsersDatabase.updateUser(Integer.parseInt(txtId.getText()), txtName.getText(), txtLastName.getText(), txtEmail.getText(),
                    txtUserName.getText(), EncryptPassword.encryptSHA256(txtPassword.getText()), role);

            if (success) {
                Message.showInfo("Success", "User updated successfully.");
                refreshData();
                Stage mainStage = (Stage) txtUserName.getScene().getWindow();
                mainStage.close();
            } else {
                Message.showError("Error", "An error occurred while updating the user.");
            }
        }
    }
}
