package com.example.hospital.model;

public record DoctorId(String value) {
    public DoctorId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("DoctorId cannot be blank");
    }
}
