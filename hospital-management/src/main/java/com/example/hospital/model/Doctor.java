package com.example.hospital.model;

public record Doctor(String id, String name) {
    public Doctor {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
    }
}
