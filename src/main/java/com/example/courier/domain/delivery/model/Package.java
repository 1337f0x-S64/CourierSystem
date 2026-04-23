package com.example.courier.domain.delivery.model;

import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.domain.valueobjects.TrackingNumber;

import java.util.Objects;

//Aggregate Root for the Package domain. Encapsulates package-related data and business logic.
public class Package {
    private final Integer id;
    private final UserId ownerId;
    private double weight;
    private String description;
    private TrackingNumber trackingNumber;
    private DeliveryStatus status;

    // Constructor for creating a new Package
    public Package(Integer id, UserId ownerId, double weight, String description) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Package owner ID cannot be null.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Package weight must be positive.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Package description cannot be empty.");
        }

        this.id = id;
        this.ownerId = ownerId;
        this.weight = weight;
        this.description = description;
        this.trackingNumber = TrackingNumber.generate();
        this.status = DeliveryStatus.registered();
    }

    // Constructor for loading an existing Package
    public Package(Integer id, UserId ownerId, double weight, String description,
                   TrackingNumber trackingNumber, DeliveryStatus status) {
        this(id, ownerId, weight, description);
        if (trackingNumber == null) throw new IllegalArgumentException("Tracking number cannot be null.");
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    // Business Behavior
    public void ship() {
        this.status = this.status.transitionTo(DeliveryStatus.inTransit());
    }

    public void deliver() {
        this.status = this.status.transitionTo(DeliveryStatus.delivered());
    }

    public void updateWeight(double newWeight) {
        if (newWeight <= 0) {
            throw new IllegalArgumentException("Weight must be positive.");
        }
        this.weight = newWeight;
    }

    public void updateDescription(String newDescription) {
        if (newDescription == null || newDescription.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        this.description = newDescription;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return Objects.equals(id, aPackage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
