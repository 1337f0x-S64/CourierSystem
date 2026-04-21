package com.example.courier.modelsPersonalize;

import java.util.Objects;
import java.util.UUID;

//Value Object representing a Tracking Number.

public final class TrackingNumber {
    private final String value;

    public TrackingNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tracking number cannot be empty.");
        }
        this.value = value;
    }

    public static TrackingNumber generate() {
        return new TrackingNumber("RP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }


    public static TrackingNumber of(String value) {
        return new TrackingNumber(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingNumber that = (TrackingNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
