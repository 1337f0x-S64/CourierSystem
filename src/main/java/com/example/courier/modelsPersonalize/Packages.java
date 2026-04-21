package com.example.courier.modelsPersonalize;
import javafx.beans.property.*;

public class Packages {

    private IntegerProperty id;
    private IntegerProperty userId;
    private StringProperty nameUser;
    private DoubleProperty weight;
    private StringProperty description;
    private StringProperty trackingNumber;
    private StringProperty status;

    public Packages(int id, int userId, String nameUser, double weight, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.userId = new SimpleIntegerProperty(userId);
        this.nameUser = new SimpleStringProperty(nameUser);
        this.weight = new SimpleDoubleProperty(weight);
        this.description = new SimpleStringProperty(description);
        this.trackingNumber = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public void setNameUser(String nameUser) {
        this.nameUser.set(nameUser);
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber.set(trackingNumber);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getId() {
        return id.get();
    }

    public int getUserId() {
        return userId.get();
    }

    public String getNameUser() {
        return nameUser.get();
    }

    public double getWeight() {
        return weight.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getTrackingNumber() {
        return trackingNumber.get();
    }

    public String getStatus() {
        return status.get();
    }

    public IntegerProperty idProperty(){
        return id;
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public StringProperty nameUserProperty() {
        return nameUser;
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty trackingNumberProperty() {
        return trackingNumber;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
