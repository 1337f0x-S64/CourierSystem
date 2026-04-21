package com.example.courier.domain.valueobjects;


 // Enforces Business Rule: Delivery status must follow a valid lifecycle (Registered, In Transit, Delivered).

public final class DeliveryStatus {

    public enum Status { REGISTERED, IN_TRANSIT, DELIVERED }

    private final Status value;

    private DeliveryStatus(Status value) {
        this.value = value;
    }

    //Factory methods aligned with Ubiquitous Language
    public static DeliveryStatus registered() { return new DeliveryStatus(Status.REGISTERED); }
    public static DeliveryStatus inTransit()   { return new DeliveryStatus(Status.IN_TRANSIT); }
    public static DeliveryStatus delivered()   { return new DeliveryStatus(Status.DELIVERED); }

    //it maps the database code ("w", "p", "d") to a DeliveryStatus.
    public static DeliveryStatus fromCode(String code) {
        return switch (code) {
            case "w" -> registered();
            case "p" -> inTransit();
            case "d" -> delivered();
            default  -> throw new IllegalArgumentException("Unknown delivery status code: " + code);
        };
    }

    public String toCode() {
        return switch (value) {
            case REGISTERED -> "w";
            case IN_TRANSIT -> "p";
            case DELIVERED  -> "d";
        };
    }

    //I t enforces Business Rule: status transitions must follow the valid lifecycle.
    public DeliveryStatus transitionTo(DeliveryStatus next) {
        boolean valid = (this.value == Status.REGISTERED && next.value == Status.IN_TRANSIT)
                     || (this.value == Status.IN_TRANSIT  && next.value == Status.DELIVERED);
        if (!valid) {
            throw new IllegalStateException(
                "Invalid status transition from " + this.value + " to " + next.value);
        }
        return next;
    }

    public boolean isDelivered()  { return value == Status.DELIVERED; }
    public boolean isInTransit()  { return value == Status.IN_TRANSIT; }
    public boolean isRegistered() { return value == Status.REGISTERED; }

    @Override
    public String toString() {
        return switch (value) {
            case REGISTERED -> "Registered";
            case IN_TRANSIT -> "In Transit";
            case DELIVERED  -> "Delivered";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryStatus other)) return false;
        return value == other.value;
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
