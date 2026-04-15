package com.example.hospital.model;

import java.time.Instant;

public record TreatmentRecord(String consultationId, String diagnosis, String prescription, Instant createdAt) {
    public TreatmentRecord {
        if (consultationId == null) throw new IllegalArgumentException("consultationId required");
        if (diagnosis == null || diagnosis.isBlank()) throw new IllegalArgumentException("diagnosis required");
        if (createdAt == null) throw new IllegalArgumentException("createdAt required");
    }
}
