package com.example.courier.controllers;

import com.example.courier.domain.valueobjects.UserRole;
import com.example.courier.models.UserGlobal;
import com.example.courier.utils.messages.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    @FXML
    private Button btnMyUser;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnTracking;

    @FXML
    private Button btnPackage;

    @FXML
    private Button btnDelivery;

    @FXML
    private Button btnCloseSession;

    @FXML
    private StackPane contentViewMain;

    public void initialize() {

        btnMyUser.setGraphic(loadIconImage("user_package.png"));
        btnUsers.setGraphic(loadIconImage("users.png"));
        btnPackage.setGraphic(loadIconImage("pack.png"));
        btnTracking.setGraphic(loadIconImage("transport.png"));
        btnDelivery.setGraphic(loadIconImage("OIPI.png"));
        btnCloseSession.setGraphic(loadIconImage("shutdown.png"));


        btnMyUser.setOnAction(e -> loadViews("MyUserViews.fxml"));
        btnUsers.setOnAction(e -> loadViews("UsersViews.fxml"));
        btnPackage.setOnAction(e -> loadViews("Packages.fxml"));
        btnTracking.setOnAction(e -> loadViews("TrackingViews.fxml"));
        btnDelivery.setOnAction(e -> loadViews("DeliveryViews.fxml"));
        btnCloseSession.setOnAction(e -> CloseSession());

        // Use UserRole domain logic instead of hardcoded strings
        UserRole currentRole = UserRole.fromName(UserGlobal.getInstance().getRol());

        // Reset visibility for all buttons first to ensure a clean state
        btnUsers.setVisible(false);
        btnPackage.setVisible(false);
        btnDelivery.setVisible(false);
        btnTracking.setVisible(true);

        btnUsers.setManaged(false);
        btnPackage.setManaged(false);
        btnDelivery.setManaged(false);
        btnTracking.setManaged(true);

        if (currentRole.hasFullAccess()) {
            setButtonVisible(btnUsers, true);
            setButtonVisible(btnPackage, true);
            setButtonVisible(btnDelivery, true);
            setButtonVisible(btnTracking, true);
        } else if (currentRole.isCourier()) {
            setButtonVisible(btnPackage, true);
            setButtonVisible(btnDelivery, true);
            setButtonVisible(btnTracking, true);
        }
        // the client has only btnTracking visible
    }

    private void setButtonVisible(Button btn, boolean visible) {
        btn.setVisible(visible);
        btn.setManaged(visible);
    }
    private void CloseSession () {
        Message.showYesOrNot("Close Session", "Do you want to close the session?", this::closeWindowsMenu);
    }

    private void closeWindowsMenu() {
        Stage mainStage = (Stage) btnMyUser.getScene().getWindow();
        mainStage.close();
    }

    private ImageView loadIconImage(String nameIcon) {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/courier/images/" + nameIcon)));
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        return imageView;
    }

    private void loadViews(String nameFileFxml) {
        try {
            FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("/com/example/courier/views/" + nameFileFxml));
            Pane newView = viewLoader.load();

            contentViewMain.getChildren().setAll(newView);
        } catch (IOException  e) {
            e.printStackTrace();
        }

    }
}
