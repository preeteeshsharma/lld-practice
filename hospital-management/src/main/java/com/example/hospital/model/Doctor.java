package com.example.hospital.model;

public record Doctor(DoctorId id, String name) {
    public Doctor {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Doctor name cannot be blank");
    }
}
