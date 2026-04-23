package com.example.courier.controllers;
import com.example.courier.controllers.interfaces.ConfigurableController;
import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.domain.repositories.PackageRepository;
import com.example.courier.infrastructure.repositories.PackageRepositoryImpl;
import com.example.courier.utils.ImagesLoader;
import com.example.courier.utils.formats.TextFormat;
import com.example.courier.utils.messages.Message;
import com.example.courier.utils.windows.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import java.util.Optional;

import static com.example.courier.controllers.PackagesController.refreshData;

public class AddEditPackageController implements ConfigurableController {

    // DDD: depend on the repository interface and not databse class
    private final PackageRepository packageRepository = new PackageRepositoryImpl();

    @FXML private TextField txtId;
    @FXML private TextField txtUserId;
    @FXML public  Button   btnSearchUser;
    @FXML private TextField txtNameUser;
    @FXML private TextField txtWeight;
    @FXML private TextField txtDescription;
    @FXML private Button   btnSave;

    private static AddEditPackageController activeInstance;

    public AddEditPackageController() { activeInstance = this; }

    @Override
    public void setParameter(Object params) {
        if (params instanceof Integer packageId) {
            loadData(packageId);
        }
    }

    // DDD: load through repository — returns domain Package, then populate UI fields
    private void loadData(int packageId) {
        packageRepository.findById(packageId).ifPresentOrElse(pkg -> {
            txtId.setText(String.valueOf(pkg.getId()));
            txtUserId.setText(String.valueOf(pkg.getOwnerId().getValue()));
            txtWeight.setText(String.valueOf(pkg.getWeight()));
            txtDescription.setText(pkg.getDescription());
        }, () -> Message.showError("Error", "Package not found."));
    }

    public void initialize() {
        txtId.setManaged(false);
        txtUserId.setManaged(false);
        btnSearchUser.setGraphic(ImagesLoader.loadIconImage("search.png", 15, 15));

        txtUserId.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtNameUser.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtWeight.setTextFormatter(new TextFormatter<>(TextFormat.textLength(10)));
        txtDescription.setTextFormatter(new TextFormatter<>(TextFormat.textLength(100)));

        btnSave.setOnAction(_ -> savePackage());
        btnSearchUser.setOnAction(e -> showUsers());
    }

    public static void addUser(String id, String name) {
        if (activeInstance != null) {
            activeInstance.txtUserId.setText(id);
            activeInstance.txtNameUser.setText(name);
        }
    }

    private void showUsers() {
        Windows.newWindowModalParams("/com/example/courier/views/SearchUsers.fxml",
                "Users", 300, 500, Optional.of(false), "user_package.png", "");
    }

    private void savePackage() {
        if (txtUserId.getText().isEmpty()) { Message.showError("Package Error", "User ID is required."); return; }
        if (txtWeight.getText().isEmpty())  { Message.showError("Package Error", "Weight is required."); return; }
        if (!txtWeight.getText().matches("\\d+(\\.\\d+)?")) { Message.showError("Package Error", "Weight must be a number."); return; }
        if (txtDescription.getText().isEmpty()) { Message.showError("Package Error", "Description is required."); return; }

        try {
            int    userId      = Integer.parseInt(txtUserId.getText());
            double weight      = Double.parseDouble(txtWeight.getText());
            String description = txtDescription.getText();

            boolean success;
            String  message;

            if (txtId.getText().isEmpty()) {
                // DDD: build a domain Package aggregate — invariants enforced in constructor
                Package pkg = new Package(null, new UserId(userId), weight, description);
                success = packageRepository.save(pkg);
                message = "Package created successfully.";
            } else {
                int id = Integer.parseInt(txtId.getText());
                Package existing = packageRepository.findById(id).orElseThrow();
                Package pkg = new Package(id, new UserId(userId), weight, description,
                        existing.getTrackingNumber(), existing.getStatus());
                success = packageRepository.update(pkg);
                message = "Package updated successfully.";
            }

            if (success) {
                Message.showInfo("Success", message);
                refreshData();
                ((Stage) txtNameUser.getScene().getWindow()).close();
            } else {
                Message.showError("Error", "An error occurred while saving the package.");
            }
        } catch (NumberFormatException e) {
            Message.showError("Package Error", "Invalid data format.");
        }
    }
}