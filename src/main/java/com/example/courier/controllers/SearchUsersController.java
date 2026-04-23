package com.example.courier.controllers;

import com.example.courier.controllers.interfaces.ConfigurableController;
import com.example.courier.db.UsersDatabase;
import com.example.courier.models.Users;
import com.example.courier.utils.ImagesLoader;
import com.example.courier.utils.messages.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SearchUsersController implements ConfigurableController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnClean;

    @FXML
    private Button btnSelect;

    @FXML
    private TableView<Users> tableUsers;

    @FXML
    TableColumn<Users, Integer> colId;

    @FXML
    private TableColumn<Users, String> colName;

    @FXML
    private TableColumn<Users, String> colLastName;

    private String paramWindow;

    @Override
    public void setParameter(Object params) {
        if (params instanceof String) {
            paramWindow = (String) params;
            loadData();
        }
    }

    public void initialize() {
        btnSearch.setGraphic(ImagesLoader.loadIconImage("search.png", 15, 15));
        btnClean.setGraphic(ImagesLoader.loadIconImage("delete.png", 15, 15));

        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastnameProperty());
        loadData();

        btnSearch.setOnAction(e -> loadData());
        btnSelect.setOnAction(e -> selectUser());
        btnClean.setOnAction(e -> cleanData());
    }

    private void loadData() {
        tableUsers.getItems().clear();
        ObservableList<Users> users = UsersDatabase.loadUsers(txtSearch.getText(), paramWindow);
        tableUsers.setItems(users);
    }
    private void selectUser() {
        Users user = tableUsers.getSelectionModel().getSelectedItem();

        if (user != null) {
            if (paramWindow.equals("Courier")) {
                AddEditDeliveryController.addCourier(String.valueOf(user.getId()), user.getName());
            } else {
                AddEditPackageController.addUser(String.valueOf(user.getId()), user.getName());
            }
            closeWindows();
        } else {
            Message.showError("Users", "You must select a user.");
        }
    }
    private void cleanData() {
        txtSearch.setText("");
        loadData();
    }
    private void closeWindows() {
        Stage mainStage = (Stage) txtSearch.getScene().getWindow();
        mainStage.close();
    }

}
