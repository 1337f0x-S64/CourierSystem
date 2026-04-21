package com.example.courier.controllers;

import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.repositories.PackageRepository;
import com.example.courier.infrastructure.repositories.PackageRepositoryImpl;
import com.example.courier.modelsPersonalize.Packages;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.windows.Windows;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public class PackagesController {
    // DDD: depend on the repository interface and not the database class
    private final PackageRepository packageRepository = new PackageRepositoryImpl();

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

    @FXML
    private TableColumn<Packages, String> colTrackingNumber;

    @FXML
    private TableColumn<Packages, String> colStatus;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    static PackagesController activeInstance;

    public PackagesController() {
        activeInstance = this;
    }

    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colUserId.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        colNameUser.setCellValueFactory(cellData -> cellData.getValue().nameUserProperty());
        colWeight.setCellValueFactory(cellData -> cellData.getValue().weightProperty().asObject());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        
        if (colTrackingNumber != null)
            colTrackingNumber.setCellValueFactory(cellData -> cellData.getValue().trackingNumberProperty());
        if (colStatus != null)
            colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        colUserId.setVisible(false);

        loadTable();

        btnAdd.setOnAction(e -> addPackage());
        btnEdit.setOnAction(e -> editPackage());
        btnDelete.setOnAction(e -> deletePackage());
    }

    // DDD: repository returns domain Package objects. we map to UI Packages for the table
    private void loadTable() {
        List<Package> domainPackages = packageRepository.findAll();
        List<Packages> uiPackages = domainPackages.stream()
                .map(p -> new Packages(
                        p.getId(),
                        p.getOwnerId().getValue(),
                        "",
                        p.getWeight(),
                        p.getDescription()
                ))
                .toList();
        tablePackages.setItems(FXCollections.observableArrayList(uiPackages));
    }

    public static void refreshData() {
        if (activeInstance != null) {
            activeInstance.loadTable();
        }
    }

    private void addPackage() {
        String route = "/com/example/courier/views/AddEditPackage.fxml";
        Windows.newWindowModal(route, "Add New Package", 240, 300, Optional.of(false), "package.png");
    }

    private void editPackage() {
        Packages pkg = tablePackages.getSelectionModel().getSelectedItem();
        String route = "/com/example/courier/views/AddEditPackage.fxml";
        if (pkg != null) {
            Windows.newWindowModalParams(route, "Edit Package", 240, 300, Optional.of(false), "editor.png", pkg.getId());
        } else {
            Message.showError("Packages", "You must select a package.");
        }
    }

    private void deletePackage() {
        Packages pkg = tablePackages.getSelectionModel().getSelectedItem();
        if (pkg != null) {
            boolean confirmed = Message.showsConfirm("Confirm Deletion", "Are you sure you want to delete the selected package?");
            if (confirmed) {
                // DDD: delete goes through the repository interface
                boolean success = packageRepository.delete(pkg.getId());
                if (success) {
                    Message.showInfo("Success", "Package deleted successfully.");
                    loadTable();
                }
            } else {
                Message.showInfo("Cancelled", "Deletion was cancelled.");
            }
        } else {
            Message.showError("Packages", "You must select a package.");
        }
    }
}
