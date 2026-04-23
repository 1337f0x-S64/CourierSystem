package com.example.courier.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ImagesLoader {
    public static ImageView loadIconImage(String nameIcon, int width, int height) {
        Image icon = new Image(Objects.requireNonNull(ImagesLoader.class.getResourceAsStream("/com/example/courier/images/" + nameIcon)));
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(width);
        imageView.setFitWidth(height);
        return imageView;
    }
}
