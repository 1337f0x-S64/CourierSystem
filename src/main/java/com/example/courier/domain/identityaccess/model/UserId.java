package com.example.courier.domain.identityaccess.model;

import java.util.Objects;

// value object representing a User's ID.

public final class UserId {
    private final int value;

    public UserId(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("User ID must be a positive integer.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value == userId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
