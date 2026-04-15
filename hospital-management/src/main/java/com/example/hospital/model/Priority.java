package com.example.hospital.model;

public enum Priority {
    EMERGENCY(0),
    GENERAL(1);

    private final int rank;

    Priority(int rank) {
        this.rank = rank;
    }

    public int rank() {
        return rank;
    }
}
