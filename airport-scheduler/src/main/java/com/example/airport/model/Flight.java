package com.example.airport.model;

public record Flight(String id, String callsign, Priority basePriority) {
    public Flight {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (callsign == null || callsign.isBlank()) throw new IllegalArgumentException("callsign required");
        if (basePriority == null) throw new IllegalArgumentException("basePriority required");
    }
}
