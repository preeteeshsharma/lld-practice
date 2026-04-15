package com.example.airport.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

// Context wrapping a Flight with its current (possibly aged) effective rank.
// Mutable by design: aging policy bumps effectiveRank in place without allocating
// a new request, so queue identity is preserved.
public final class FlightRequest {
    private final Flight flight;
    private final Instant requestedAt;
    private final AtomicInteger effectiveRank;

    public FlightRequest(Flight flight, Instant requestedAt) {
        if (flight == null) throw new IllegalArgumentException("flight required");
        if (requestedAt == null) throw new IllegalArgumentException("requestedAt required");
        this.flight = flight;
        this.requestedAt = requestedAt;
        this.effectiveRank = new AtomicInteger(flight.basePriority().rank());
    }

    public Flight flight() {
        return flight;
    }

    public Instant requestedAt() {
        return requestedAt;
    }

    public int effectiveRank() {
        return effectiveRank.get();
    }

    // Decrease rank value by 1 (i.e. promote), capped at 0 (EMERGENCY level).
    // Returns true if the rank actually changed.
    public boolean bumpPriority() {
        int before = effectiveRank.get();
        int after = effectiveRank.updateAndGet(r -> Math.max(0, r - 1));
        return before != after;
    }
}
