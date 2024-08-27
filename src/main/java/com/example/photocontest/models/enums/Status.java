package com.example.photocontest.models.enums;

public enum Status {
    OPEN,
    INVITATIONAL;

    public String toString() {
        switch (this) {
            case OPEN:
                return "Open";
            case INVITATIONAL:
                return "Invitational";
            default:
                throw new IllegalArgumentException("Unexpected value: " + this);
        }
    }
}
