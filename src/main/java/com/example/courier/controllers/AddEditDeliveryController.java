package com.example.courier.controllers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static com.example.courier.controllers.DeliveryViewsController.refreshData;
import com.example.courier.controllers.interfaces.ConfigurableController;
import com.example.courier.domain.repositories.RouteRepository;
import com.example.courier.domain.services.RouteOptimizationService;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.domain.valueobjects.TrackingNumber;
import com.example.courier.infrastructure.repositories.RouteRepositoryImpl;
import com.example.courier.modelsPersonalize.Routes;
import com.example.courier.utils.ImagesLoader;
import com.example.courier.utils.formats.FormatDate;
import com.example.courier.utils.formats.SpinnersFormat;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.windows.Windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class AddEditDeliveryController implements ConfigurableController {

    private final RouteRepository          routeRepository     = new RouteRepositoryImpl();
    private final RouteOptimizationService optimizationService = new RouteOptimizationService();

    @FXML private TextField  txtId;
    @FXML private TextField  txtPackageId;
    @FXML private TextField  txtPackageDescription;
    @FXML private Button     btnSearchPackage;
    @FXML private TextField  txtStartLocation;
    @FXML private TextField  txtEndLocation;
    @FXML private TextField  txtTrackingNumber;
    @FXML private DatePicker txtDeliveryDate;
    @FXML private Spinner    cbxHours;
    @FXML private Spinner    cbxMinutes;
    @FXML private TextField  txtOptimalPath;
    @FXML private ComboBox   txtStatus;
    @FXML private TextField  txtCourierId;
    @FXML private TextField  txtCourierName;
    @FXML private Button     btnSearchCourier;
    @FXML private DatePicker txtPossibleReceptionDate;
    @FXML private Button     btnSave;

    static AddEditDeliveryController activeInstance;

    public AddEditDeliveryController() {
        activeInstance = this;
    }

    @Override
    public void setParameter(Object params) {
        if (params instanceof Integer id) {
            loadData(id);
        }
    }

    private void loadData(int id) {
        
        Routes route = routeRepository.findById(id);
        if (route == null) {
            Message.showError("Error", "Route not found.");
            return;
        }

        Timestamp dateDelivery = route.getDeliveryDate();
        txtId.setText(String.valueOf(route.getId()));
        txtPackageId.setText(String.valueOf(route.getPackageId()));
        txtPackageDescription.setText(route.getPackageName());
        txtStartLocation.setText(route.getStartLocation());
        txtEndLocation.setText(route.getEndLocation());
        txtTrackingNumber.setText(route.getTrackingNumber());
        txtOptimalPath.setText(route.getOptimalPath());
        txtCourierName.setText(route.getCourierName());
        txtCourierId.setText(String.valueOf(route.getCourierId()));

        LocalDateTime deliveryLdt = (dateDelivery != null)
                ? dateDelivery.toLocalDateTime()
                : LocalDateTime.now();
        txtDeliveryDate.setValue(deliveryLdt.toLocalDate());
        cbxHours.setValueFactory(
                SpinnersFormat.FormatTime(0, 23, deliveryLdt.getHour()));
        cbxMinutes.setValueFactory(
                SpinnersFormat.FormatTime(0, 59, deliveryLdt.getMinute()));

        java.sql.Date possibleDate = route.getPossibleReceptionDate();
        txtPossibleReceptionDate.setValue(
                possibleDate != null ? possibleDate.toLocalDate() : LocalDate.now());

        txtStatus.getSelectionModel().select(route.getStatus());
    }

    public void initialize() {
        LocalDateTime timeNow = LocalDateTime.now();

        txtPackageId.setVisible(false);
        txtCourierId.setVisible(false);
        txtPackageId.setManaged(false);
        txtCourierId.setManaged(false);

        btnSearchPackage.setGraphic(ImagesLoader.loadIconImage("search.png", 15, 15));
        btnSearchCourier.setGraphic(ImagesLoader.loadIconImage("search.png", 15, 15));

        txtStartLocation.setTextFormatter(new TextFormatter<>(TextFormat.textLength(100)));
        txtEndLocation.setTextFormatter(new TextFormatter<>(TextFormat.textLength(100)));
        txtTrackingNumber.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtOptimalPath.setTextFormatter(new TextFormatter<>(TextFormat.textLength(30)));

        FormatDate.formatCalendar(txtDeliveryDate);
        FormatDate.formatCalendar(txtPossibleReceptionDate);
        txtDeliveryDate.setValue(LocalDate.now());
        txtPossibleReceptionDate.setValue(LocalDate.now());

        cbxHours.setValueFactory(SpinnersFormat.FormatTime(0, 23, timeNow.getHour()));
        cbxMinutes.setValueFactory(SpinnersFormat.FormatTime(0, 59, timeNow.getMinute()));

        txtStatus.getItems().addAll("waiting", "progress", "delivered");

        txtId.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isNewRoute = (newVal == null || newVal.isEmpty());
            txtStatus.setDisable(isNewRoute);
            if (isNewRoute) {
                txtStatus.getSelectionModel().select("waiting");
            }
        });
        txtStatus.setDisable(true);
        txtStatus.getSelectionModel().select("waiting");

        txtStartLocation.focusedProperty().addListener(
                (obs, wasFocused, isNowFocused) -> fillOptimalPath());
        txtEndLocation.focusedProperty().addListener(
                (obs, wasFocused, isNowFocused) -> fillOptimalPath());

        btnSave.setOnAction(e -> saveRoute());
        btnSearchPackage.setOnAction(e -> showPackages());
        btnSearchCourier.setOnAction(e -> showCourier());
    }

    private void fillOptimalPath() {
        String start = txtStartLocation.getText();
        String end   = txtEndLocation.getText();
        if (!start.isBlank() && !end.isBlank()) {
            txtOptimalPath.setText(optimizationService.calculateOptimalPath(start, end));
        }
    }

    private void showPackages() {
        Windows.newWindowModal(
                "/com/example/courier/views/SearchPackage.fxml",
                "Package", 300, 500, Optional.of(false), "package.png");
    }

    private void showCourier() {
        Windows.newWindowModalParams(
                "/com/example/courier/views/SearchUsers.fxml",
                "Users", 300, 500, Optional.of(false), "user_package.png", "Courier");
    }

    public static void addPackage(String id, String description) {
        if (activeInstance != null) {
            activeInstance.txtPackageId.setText(id);
            activeInstance.txtPackageDescription.setText(description);
        }
    }

    public static void addCourier(String id, String description) {
        if (activeInstance != null) {
            activeInstance.txtCourierId.setText(id);
            activeInstance.txtCourierName.setText(description);
        }
    }

    private void saveRoute() {


        if (txtPackageId.getText().isEmpty()) {
            Message.showError("Route Error", "Package ID is required.");
            return;
        }
        if (txtStartLocation.getText().isEmpty()) {
            Message.showError("Route Error", "Start location is required.");
            return;
        }
        if (txtEndLocation.getText().isEmpty()) {
            Message.showError("Route Error", "End location is required.");
            return;
        }
        if (txtTrackingNumber.getText().isEmpty()) {
            Message.showError("Route Error", "Tracking number is required.");
            return;
        }
        if (!txtTrackingNumber.getText().matches("\\d+")) {
            Message.showError("Route Error",
                    "Tracking number must contain only numbers.");
            return;
        }
        if (txtDeliveryDate.getValue() == null) {
            Message.showError("Route Error", "Delivery date is required.");
            return;
        }
        if (txtStatus.getValue() == null) {
            Message.showError("Route Error", "Select a status.");
            return;
        }
        if (txtCourierName.getText().isEmpty()) {
            Message.showError("Route Error", "Courier is required.");
            return;
        }
        if (txtPossibleReceptionDate.getValue() == null) {
            Message.showError("Route Error", "Reception date is required.");
            return;
        }

        try {
            int    packageId   = Integer.parseInt(txtPackageId.getText());
            int    courierId   = Integer.parseInt(txtCourierId.getText());
            String start       = txtStartLocation.getText();
            String end         = txtEndLocation.getText();
            String tracking    = txtTrackingNumber.getText();
            String optimalPath = optimizationService.calculateOptimalPath(start, end);


            String rawStatus = (String) txtStatus.getValue();
            String statusCode;

            if (txtId.getText().isEmpty()) {
                statusCode = DeliveryStatus.registered().toCode();

            } else {
                int    existingId = Integer.parseInt(txtId.getText());
                Routes existing   = routeRepository.findById(existingId);

                if (existing == null) {
                    Message.showError("Route Error",
                            "Could not load the existing route to validate status.");
                    return;
                }

                DeliveryStatus currentStatus = existing.getDeliveryStatus();

                DeliveryStatus requestedStatus = switch (rawStatus) {
                    case "waiting"   -> DeliveryStatus.registered();
                    case "progress"  -> DeliveryStatus.inTransit();
                    case "delivered" -> DeliveryStatus.delivered();
                    default -> throw new IllegalArgumentException(
                            "Unknown status value: " + rawStatus);
                };

                if (!currentStatus.equals(requestedStatus)) {
                    try {
                        currentStatus.transitionTo(requestedStatus);
                    } catch (IllegalStateException e) {
                        Message.showError("Status Error",
                                "Invalid status transition: cannot move from \""
                                + currentStatus + "\" to \""
                                + requestedStatus + "\".");
                        return;
                    }
                }
                statusCode = requestedStatus.toCode();
            }


            Integer   hour     = (Integer) cbxHours.getValue();
            Integer   minute   = (Integer) cbxMinutes.getValue();
            Timestamp deliveryDateTime = Timestamp.valueOf(
                    LocalDateTime.of(txtDeliveryDate.getValue(),
                            LocalTime.of(hour, minute)));
            Timestamp receptionDate = Timestamp.valueOf(
                    LocalDateTime.of(txtPossibleReceptionDate.getValue(),
                            LocalTime.MIDNIGHT));


            boolean success;
            String  message;

            if (txtId.getText().isEmpty()) {
                
                TrackingNumber tn = TrackingNumber.of(tracking);
                success = routeRepository.save(
                        packageId, start, end, tn.getValue(),
                        optimalPath, statusCode, courierId,
                        deliveryDateTime, receptionDate);
                message = "Route created successfully.";
            } else {
                success = routeRepository.update(
                        Integer.parseInt(txtId.getText()),
                        packageId, start, end, tracking,
                        optimalPath, statusCode, courierId,
                        receptionDate, deliveryDateTime);
                message = "Route updated successfully.";
            }

            if (success) {
                Message.showInfo("Success", message);
                refreshData();
                ((Stage) txtPackageId.getScene().getWindow()).close();
            } else {
                Message.showError("Error",
                        "An error occurred while saving the route.");
            }

        } catch (NumberFormatException e) {
            Message.showError("Route Error", "Invalid data format.");
        }
    }
}