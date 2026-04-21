module com.example.courier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens com.example.courier to javafx.fxml;
    exports com.example.courier;

    opens com.example.courier.db to javafx.fxml;
    exports com.example.courier.db;

    opens com.example.courier.controllers to javafx.fxml;
    exports com.example.courier.controllers;
    exports com.example.courier.controllers.interfaces;
    opens com.example.courier.controllers.interfaces to javafx.fxml;
    exports com.example.courier.models;
    opens com.example.courier.models to javafx.fxml;
    exports com.example.courier.modelsPersonalize;
    opens com.example.courier.modelsPersonalize to javafx.fxml;

    // DDD Domain Layer
    exports com.example.courier.domain.valueobjects;
    exports com.example.courier.domain.repositories;
    exports com.example.courier.domain.services;

    // DDD Infrastructure Layer
    exports com.example.courier.infrastructure.repositories;
}