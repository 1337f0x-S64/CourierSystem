package com.example.courier.controllers;

import com.example.courier.domain.repositories.RouteRepository;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.infrastructure.repositories.RouteRepositoryImpl;
import com.example.courier.modelsPersonalize.Routes;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.windows.Windows;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Timestamp;
import java.util.Optional;

public class DeliveryViewsController {

    // DDD: depend on the repository interface and not the dabase class
    private final RouteRepository routeRepository = new RouteRepositoryImpl();

    @FXML TableView<Routes> tableRoutes;
    @FXML private TableColumn<Routes, Integer> colId;
    @FXML private TableColumn<Routes, Integer> colPackageId;
    @FXML private TableColumn<Routes, String>  colPackageName;
    @FXML private TableColumn<Routes, String>  colStartLocation;
    @FXML private TableColumn<Routes, String>  colEndLocation;
    @FXML private TableColumn<Routes, String>  colTrackingNumber;
    @FXML private TableColumn<Routes, String>  colDeliveryDate;
    @FXML private TableColumn<Routes, String>  colOptimalPath;
    @FXML private TableColumn<Routes, String>  colStatus;
    @FXML private TableColumn<Routes, Integer> colCourierId;
    @FXML private TableColumn<Routes, String>  colCourierName;
    @FXML private TableColumn<Routes, String>  colPossibleReceptionDate;
    @FXML private TableColumn<Routes, String>  colReceptionDate;
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnCompleteDelivery;

    static DeliveryViewsController activeInstance;

    public DeliveryViewsController() { activeInstance = this; }

    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colPackageId.setCellValueFactory(cellData -> cellData.getValue().packageIdProperty().asObject());
        colPackageName.setCellValueFactory(cellData -> cellData.getValue().packageNameProperty());
        colStartLocation.setCellValueFactory(cellData -> cellData.getValue().startLocationProperty());
        colEndLocation.setCellValueFactory(cellData -> cellData.getValue().endLocationProperty());
        colTrackingNumber.setCellValueFactory(cellData -> cellData.getValue().trackingNumberProperty());
        colDeliveryDate.setCellValueFactory(cellData -> cellData.getValue().deliveryDateProperty().asString());
        colOptimalPath.setCellValueFactory(cellData -> cellData.getValue().optimalPathProperty());
        // DDD: getStatus() on Routes already delegates to DeliveryStatus.fromCode().toString()
        colStatus.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
        colCourierId.setCellValueFactory(cellData -> cellData.getValue().courierIdProperty().asObject());
        colCourierName.setCellValueFactory(cellData -> cellData.getValue().courierNameProperty());
        colPossibleReceptionDate.setCellValueFactory(cellData -> cellData.getValue().possibleReceptionDateProperty().asString());
        colReceptionDate.setCellValueFactory(cellData -> {
            Timestamp date = cellData.getValue().getReceptionDate();
            return new SimpleStringProperty(date != null ? date.toString() : "");
        });

        colPackageId.setVisible(false);
        colCourierId.setVisible(false);

        loadTable();

        btnAdd.setOnAction(e -> addDelivery());
        btnEdit.setOnAction(e -> editDelivery());
        btnDelete.setOnAction(e -> deleteDelivery());
        btnCompleteDelivery.setOnAction(e -> completeDelivery());
    }

    private void loadTable() {
        // DDD: load through repository interface
        ObservableList<Routes> deliveries = routeRepository.findAll();
        tableRoutes.setItems(deliveries);
    }

    public static void refreshData() {
        if (activeInstance != null) activeInstance.loadTable();
    }

    private void addDelivery() {
        Windows.newWindowModal("/com/example/courier/views/AddEditDelivery.fxml",
                "Add New Delivery", 480, 350, Optional.of(false), "route.png");
    }

    private void completeDelivery() {
        Routes selectedRoute = tableRoutes.getSelectionModel().getSelectedItem();
        if (selectedRoute == null) { Message.showError("Deliveries", "You must select a delivery."); return; }

        // DDD: enforce the transition rule before calling complete
        try {
            DeliveryStatus current = selectedRoute.getDeliveryStatus();
            current.transitionTo(DeliveryStatus.delivered()); // throws if not IN_TRANSIT
        } catch (IllegalStateException e) {
            Message.showError("Delivery Error", "Only In Transit packages can be marked as Delivered.");
            return;
        }

        boolean confirmed = Message.showsConfirm("Confirm Completion", "Are you sure you want to complete this delivery?");
        if (confirmed) {
            // DDD: complete goes through the repository interface
            boolean success = routeRepository.complete(selectedRoute.getId());
            if (success) {
                Message.showInfo("Success", "Delivery completed successfully.");
                loadTable();
            } else {
                Message.showError("Error", "An error occurred while completing the delivery.");
            }
        } else {
            Message.showInfo("Cancelled", "Completion was cancelled.");
        }
    }

    private void editDelivery() {
        Routes selectedRoute = tableRoutes.getSelectionModel().getSelectedItem();
        if (selectedRoute != null) {
            Windows.newWindowModalParams("/com/example/courier/views/AddEditDelivery.fxml",
                    "Edit Delivery", 480, 300, Optional.of(false), "route.png", selectedRoute.getId());
        } else {
            Message.showError("Deliveries", "You must select a delivery.");
        }
    }

    private void deleteDelivery() {
        Routes selectedRoute = tableRoutes.getSelectionModel().getSelectedItem();
        if (selectedRoute == null) { Message.showError("Deliveries", "You must select a delivery."); return; }

        boolean confirmed = Message.showsConfirm("Confirm Deletion", "Are you sure you want to delete the selected delivery?");
        if (confirmed) {
            // DDD: delete goes through the repository interface
            boolean success = routeRepository.delete(selectedRoute.getId());
            if (success) {
                Message.showInfo("Success", "Delivery deleted successfully.");
                loadTable();
            } else {
                Message.showError("Error", "An error occurred while deleting the delivery.");
            }
        } else {
            Message.showInfo("Cancelled", "Deletion was cancelled.");
        }
    }
}
