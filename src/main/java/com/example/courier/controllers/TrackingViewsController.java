package com.example.courier.controllers;

import com.example.courier.utils.messages.Message;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.courier.modelsPersonalize.PackageInfo;
import com.example.courier.db.TrackingViewDatabase;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class TrackingViewsController {

    @FXML
    private TextField trackingField;

    @FXML
    private TableView<PackageInfo> packageTable;

    @FXML
    private TableColumn<PackageInfo, String> idColumn;

    @FXML
    private TableColumn<PackageInfo, String> descriptionColumn;

    @FXML
    private TableColumn<PackageInfo, String> startLocationColumn;

    @FXML
    private TableColumn<PackageInfo, String> endLocationColumn;

    @FXML
    private TableColumn<PackageInfo, String> statusColumn;

    @FXML
    private TableColumn<PackageInfo, String> deliveryDateColumn;


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("trackingId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startLocationColumn.setCellValueFactory(new PropertyValueFactory<>("startLocation"));
        endLocationColumn.setCellValueFactory(new PropertyValueFactory<>("endLocation"));
        statusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    }

    @FXML
    public void trackPackage() {
        String input = trackingField.getText().trim();
        if (input.isEmpty()) {
            Message.showError("Error", "Tracking ID is required.");
            return;
        }

        try {
            String trackingId = input;
            List<PackageInfo> packageList = TrackingViewDatabase.getPackageByTrackingNumber(trackingId);
            packageTable.getItems().clear();
            if (packageList.isEmpty()) {
                Message.showError("Not Found", "Package not found.");
            } else {
                ObservableList<PackageInfo> data = FXCollections.observableArrayList(packageList);
                packageTable.setItems(data);
            }

        } catch (Exception e) {
            Message.showError("Error", "An error occurred while searching for the package.");
            e.printStackTrace();
        }
    }
}