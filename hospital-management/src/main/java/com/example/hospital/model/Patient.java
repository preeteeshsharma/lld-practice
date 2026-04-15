package com.example.hospital.model;

import java.time.Instant;

public record Patient(String id, String name, Priority priority, Instant registeredAt) {
    public Patient {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (priority == null) throw new IllegalArgumentException("priority required");
        if (registeredAt == null) throw new IllegalArgumentException("registeredAt required");
    }
}
