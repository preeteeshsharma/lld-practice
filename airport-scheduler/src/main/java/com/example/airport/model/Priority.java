package com.example.airport.model;

// rank() explicit — not ordinal() — so enum reorder cannot break the comparator.
public enum Priority {
    EMERGENCY(0),
    SCHEDULED(1),
    CARGO(2);

    private final int rank;

    Priority(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }
}
