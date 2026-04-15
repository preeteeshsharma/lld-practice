package com.example.hospital.model;

public record PatientId(String value) {
    public PatientId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("PatientId cannot be blank");
    }
}
