package com.example.courier.modelsPersonalize;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import javafx.beans.property.*;
import java.sql.Timestamp;
import java.sql.Date;

public class Routes {
    private IntegerProperty id;
    private IntegerProperty packageId;
    private StringProperty packageName;
    private StringProperty startLocation;
    private StringProperty endLocation;
    private StringProperty trackingNumber;
    private ObjectProperty<Timestamp> deliveryDate;
    private StringProperty optimalPath;
    private StringProperty status;
    private IntegerProperty courierId;
    private StringProperty courierName;
    private ObjectProperty<Date> possibleReceptionDate;
    private ObjectProperty<Timestamp> receptionDate;

    public Routes(int id, int packageId, String packageName, String startLocation, String endLocation, String trackingNumber, java.sql.Timestamp deliveryDate, String optimalPath, String status, int courierId, String courierName, java.sql.Date possibleReceptionDate, java.sql.Timestamp receptionDate) {
        this.id = new SimpleIntegerProperty(id);
        this.packageId = new SimpleIntegerProperty(packageId);
        this.packageName = new SimpleStringProperty(packageName);
        this.startLocation = new SimpleStringProperty(startLocation);
        this.endLocation = new SimpleStringProperty(endLocation);
        this.trackingNumber = new SimpleStringProperty(trackingNumber);
        this.deliveryDate = new SimpleObjectProperty<>(deliveryDate);
        this.optimalPath = new SimpleStringProperty(optimalPath);
        this.status = new SimpleStringProperty(status);
        this.courierId = new SimpleIntegerProperty(courierId);
        this.courierName = new SimpleStringProperty(courierName);
        this.possibleReceptionDate = new SimpleObjectProperty<>(possibleReceptionDate);
        this.receptionDate = new SimpleObjectProperty<>(receptionDate);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setPackageId(int packageId) {
        this.packageId.set(packageId);
    }

    public void setPackageName(String packageName) { this.packageName.set(packageName); }

    public void setStartLocation(String startLocation) {
        this.startLocation.set(startLocation);
    }

    public void setEndLocation(String endLocation) {
        this.endLocation.set(endLocation);
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber.set(trackingNumber);
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate.set(deliveryDate);
    }

    public void setOptimalPath(String optimalPath) {
        this.optimalPath.set(optimalPath);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setCourierId(int courierId) {
        this.courierId.set(courierId);
    }

    public void setCourierName(String courierName) { this.courierName.set(courierName); }

    public void setPossibleReceptionDate(Date possibleReceptionDate) {
        this.possibleReceptionDate.set(possibleReceptionDate);
    }

    public void setReceptionDate(Timestamp receptionDate) {
        this.receptionDate.set(receptionDate);
    }

    public int getId() {
        return id.get();
    }

    public int getPackageId() {
        return packageId.get();
    }

    public String getPackageName() { return packageName.get(); }

    public String getStartLocation() {
        return startLocation.get();
    }

    public String getEndLocation() {
        return endLocation.get();
    }

    public String getTrackingNumber() {
        return trackingNumber.get();
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate.getValue();
    }

    public String getOptimalPath() {
        return optimalPath.get();
    }

    // it returns the delivery status using the DeliveryStatus value object.
    //Business Rule: status must follow the valid lifecycle.

    public String getStatus() {
        try {
            return DeliveryStatus.fromCode(status.get().toLowerCase()).toString();
        } catch (IllegalArgumentException e) {
            return status.get();
        }
    }

    //Returns the DeliveryStatus value object for domain logic
    public DeliveryStatus getDeliveryStatus() {
        return DeliveryStatus.fromCode(status.get().toLowerCase());
    }

    public void transitionStatus(DeliveryStatus next) {
        DeliveryStatus current = getDeliveryStatus();
        current.transitionTo(next); // throws if invalid
        this.status.set(next.toCode());
    }

    public int getCourierId() {
        return courierId.get();
    }

    public String getCourierName() { return courierName.get(); }

    public Date getPossibleReceptionDate() {
        return possibleReceptionDate.getValue();
    }

    public Timestamp getReceptionDate() {
        return receptionDate.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty packageIdProperty() {
        return packageId;
    }

    public StringProperty packageNameProperty() { return packageName; }

    public StringProperty startLocationProperty() {
        return startLocation;
    }

    public StringProperty endLocationProperty() {
        return endLocation;
    }

    public StringProperty trackingNumberProperty() {
        return trackingNumber;
    }

    public ObjectProperty<Timestamp> deliveryDateProperty() {
        return deliveryDate;
    }

    public StringProperty optimalPathProperty() {
        return optimalPath;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public IntegerProperty courierIdProperty() {
        return courierId;
    }

    public StringProperty courierNameProperty() {
        return courierName;
    }

    public ObjectProperty<Date> possibleReceptionDateProperty() {
        return possibleReceptionDate;
    }

    public ObjectProperty<Timestamp> receptionDateProperty() {
        return receptionDate;
    }
}