package com.example.courier.modelsPersonalize;

import com.example.courier.domain.valueobjects.DeliveryStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class PackageInfo {

    private StringProperty trackingId;
    private StringProperty description;
    private StringProperty startLocation;
    private StringProperty endLocation;
    private StringProperty status;
    private StringProperty deliveryDate;

    public PackageInfo(String trackingId, String description, String startLocation, String endLocation, String status, String deliveryDate) {
        this.trackingId = new SimpleStringProperty(trackingId);
        this.description = new SimpleStringProperty(description);
        this.startLocation = new SimpleStringProperty(startLocation);
        this.endLocation = new SimpleStringProperty(endLocation);
        this.status = new SimpleStringProperty(status);
        this.deliveryDate = new SimpleStringProperty(deliveryDate);
    }

    public void setTrackingId(String trackingId) {
        this.trackingId.set(trackingId);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setStartLocation(String startLocation) {
        this.startLocation.set(startLocation);
    }

    public void setEndLocation(String endLocation) {
        this.endLocation.set(endLocation);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate.set(deliveryDate);
    }


    public String getTrackingId() {
        return trackingId.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getStartLocation() {
        return startLocation.get();
    }

    public String getEndLocation() {
        return endLocation.get();
    }

    /** Uses DeliveryStatus value object to translate the status code to Ubiquitous Language. */
    public String getStatus() {
        try {
            return DeliveryStatus.fromCode(status.get().toLowerCase()).toString();
        } catch (IllegalArgumentException e) {
            return "Unknown";
        }
    }

    public String getDeliveryDate() {
        return deliveryDate.get();
    }


    public StringProperty trackingIdProperty() {
        return trackingId;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty startLocationProperty() {
        return startLocation;
    }

    public StringProperty endLocationProperty() {
        return endLocation;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty deliveryDateProperty() {
        return deliveryDate;
    }
}
