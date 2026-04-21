package com.example.courier.controllers;

import com.example.courier.db.UsersDatabase;
import com.example.courier.models.Users;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.windows.Windows;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Optional;

public class UsersViewsController {

    @FXML
    TableView<Users> tableUsers;

    @FXML
    TableColumn<Users, Integer> colId;

    @FXML
    private TableColumn<Users, String> colName;

    @FXML
    private TableColumn<Users, String> colLastName;

    @FXML
    private TableColumn<Users, String> colUserName;

    @FXML
    private TableColumn<Users, String> colEmail;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    private static UsersViewsController activeInstance;

    public UsersViewsController() {
        activeInstance = this;
    }

    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
        colUserName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        loadData();

        btnAdd.setOnAction(e -> addUser());
        btnEdit.setOnAction(e -> editUSer());
        btnDelete.setOnAction(e -> deleteUser());
    }

    private void loadData() {
        tableUsers.getItems().clear();
        ObservableList<Users> users = UsersDatabase.loadUsers("", "");
        tableUsers.setItems(users);
    }

    public static void refreshData() {
        if (activeInstance != null) {
            activeInstance.loadData();
        }
    }

    private void addUser() {
        String route = "/com/example/courier/views/AddEditUser.fxml";
        Windows.newWindowModal(route, "Add New User", 480, 300, Optional.of(false), "add_user.png");
    }

    private void editUSer() {
        String route = "/com/example/courier/views/AddEditUser.fxml";
        Users user = tableUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            Windows.newWindowModalParams(route, "Edit New User", 480, 300, Optional.of(false), "edit_user.png", user.getId());
        } else {
            Message.showError("Users", "You must select a user.");
        }
    }

    private void deleteUser() {
        String route = "/com/example/courier/views/AddEditUser.fxml";
        Users user = tableUsers.getSelectionModel().getSelectedItem();
        if (user != null) {
            boolean confirmed = Message.showsConfirm("Confirm Deletion", "Are you sure you want to delete the selected user?");

            if (confirmed) {
                boolean success = UsersDatabase.deleteUser(user.getId());
                if (success) {
                    Message.showInfo("Success", "User deleted successfully.");
                    loadData();
                }
            }

        } else {
            Message.showError("Users", "You must select a user.");
        }
    }
}
