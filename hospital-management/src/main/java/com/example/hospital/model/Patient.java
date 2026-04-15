package com.example.hospital.model;

public record Patient(PatientId id, String name, Priority priority, int arrivalSequence) {
    public Patient {
        if (id == null) throw new IllegalArgumentException("Patient id cannot be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Patient name cannot be blank");
        if (priority == null) throw new IllegalArgumentException("Priority cannot be null");
        if (arrivalSequence < 0) throw new IllegalArgumentException("Arrival sequence must be non-negative");
    }
}
