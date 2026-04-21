package com.example.courier.controllers;

import com.example.courier.db.PackagesDatabase;
import com.example.courier.modelsPersonalize.Packages;
import com.example.courier.utils.ImagesLoader;
import com.example.courier.utils.messages.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SearchPackageController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnClean;

    @FXML
    private Button btnSelect;

    @FXML
    TableView<Packages> tablePackages;

    @FXML
    TableColumn<Packages, Integer> colId;

    @FXML
    private TableColumn<Packages, Integer> colUserId;

    @FXML
    private TableColumn<Packages, String> colNameUser;

    @FXML
    private TableColumn<Packages, Double> colWeight;

    @FXML
    private TableColumn<Packages, String> colDescription;

    public void initialize() {

        btnSearch.setGraphic(ImagesLoader.loadIconImage("search.png", 15, 15));
        btnClean.setGraphic(ImagesLoader.loadIconImage("delete.png", 15, 15));

        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colUserId.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        colNameUser.setCellValueFactory(cellData -> cellData.getValue().nameUserProperty());
        colWeight.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        loadData();

        colUserId.setVisible(false);

        btnSearch.setOnAction(e -> loadData());
        btnSelect.setOnAction(e -> selectUser());
        btnClean.setOnAction(e -> cleanData());
    }

    private void loadData() {
        tablePackages.getItems().clear();
        ObservableList<Packages> packages = PackagesDatabase.loadDataNoRoute(txtSearch.getText());
        tablePackages.setItems(packages);
    }

    private void selectUser() {
        Packages packages = tablePackages.getSelectionModel().getSelectedItem();

        if (packages != null) {
            AddEditDeliveryController.addPackage(String.valueOf(packages.getId()), packages.getDescription());
            closeWindows();
        } else {
            Message.showError("Packages", "You must select a package.");
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
