package com.example.hospital.model;

import java.time.Instant;

public record Consultation(String id, String patientId, String doctorId, Instant startedAt) {
    public Consultation {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (patientId == null) throw new IllegalArgumentException("patientId required");
        if (doctorId == null) throw new IllegalArgumentException("doctorId required");
        if (startedAt == null) throw new IllegalArgumentException("startedAt required");
    }
}
