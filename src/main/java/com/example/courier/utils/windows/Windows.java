package com.example.courier.utils.windows;

import com.example.courier.controllers.interfaces.ConfigurableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Windows {
    public static void newWindow(String routeFxml, String tittle, int height, int width, Optional<Boolean> resize, String routeIcon) {
        try {
            boolean resizeWindow = resize.orElse(true);
            FXMLLoader loader = new FXMLLoader(Windows.class.getResource(routeFxml));
            Parent root = loader.load();

            Stage window = new Stage();
            window.setTitle(tittle);
            window.setScene(new Scene(root, width , height));

            window.setResizable(resizeWindow);

            if (!routeIcon.isEmpty()) {
                window.getIcons().add(new Image(Objects.requireNonNull(Windows.class.getResourceAsStream("/com/example/courier/images/" + routeIcon))));
            }

            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void newWindowModal(String routeFxml, String tittle, int height, int width, Optional<Boolean> resize, String routeIcon) {
        try {
            boolean resizeWindow = resize.orElse(true);

            FXMLLoader loader = new FXMLLoader(Windows.class.getResource(routeFxml));
            Parent root = loader.load();


            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(tittle);
            window.setScene(new Scene(root, width , height));
            window.setResizable(resizeWindow);


            if (!routeIcon.isEmpty()) {
                window.getIcons().add(new Image(Objects.requireNonNull(Windows.class.getResourceAsStream("/com/example/courier/images/" + routeIcon))));
            }

            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void newWindowModalParams(String routeFxml, String tittle, int height, int width, Optional<Boolean> resize, String routeIcon, Object params) {
        try {
            boolean resizeWindow = resize.orElse(true);


            FXMLLoader loader = new FXMLLoader(Windows.class.getResource(routeFxml));
            Parent root = loader.load();

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(tittle);
            window.setScene(new Scene(root, width , height));
            window.setResizable(resizeWindow);

            ConfigurableController controller = loader.getController();
            controller.setParameter(params);


            if (!routeIcon.isEmpty()) {
                window.getIcons().add(new Image(Objects.requireNonNull(Windows.class.getResourceAsStream("/com/example/courier/images/" + routeIcon))));
            }

            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
