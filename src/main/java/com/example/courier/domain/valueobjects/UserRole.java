package com.example.courier.domain.valueobjects;

//Enforces Business Rule: permissions are granted based on user role.

public final class UserRole {

    public enum Role { ADMINISTRATOR, COURIER, CLIENT }

    private final Role value;

    private UserRole(Role value) { this.value = value; }

    public static UserRole administrator() { return new UserRole(Role.ADMINISTRATOR); }
    public static UserRole courier()       { return new UserRole(Role.COURIER); }
    public static UserRole client()        { return new UserRole(Role.CLIENT); }

    public static UserRole fromName(String name) {
        if (name == null || name.isBlank()) return client();
        return switch (name.trim().toLowerCase()) {
            case "administrator", "admin", "1" -> administrator();
            case "courier", "2"               -> courier();
            case "client", "customer", "3"    -> client();
            default -> throw new IllegalArgumentException("Unknown role: " + name);
        };
    }

    //Business Rule: only couriers can update delivery status.
    public boolean canUpdateDeliveryStatus() { return value == Role.COURIER; }

    //Business Rule: administrators have full system access.
    public boolean hasFullAccess() { return value == Role.ADMINISTRATOR; }

    //business Rule: clients can only view their own packages.
    public boolean isClient() { return value == Role.CLIENT; }

    public boolean isCourier() { return value == Role.COURIER; }

    public String getName() {
        return switch (value) {
            case ADMINISTRATOR -> "Administrator";
            case COURIER       -> "Courier";
            case CLIENT        -> "Client";
        };
    }

    @Override
    public String toString() { return getName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole other)) return false;
        return value == other.value;
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
