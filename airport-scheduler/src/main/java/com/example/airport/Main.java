package com.example.airport;

import com.example.airport.model.Flight;
import com.example.airport.model.FlightRequest;
import com.example.airport.model.Priority;
import com.example.airport.service.RunwayScheduler;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Age requests once they have waited >= 1s; run the aging tick every 300ms.
        RunwayScheduler scheduler = new RunwayScheduler(Duration.ofSeconds(1), 300);

        // Scenario 1: strict priority order — EMERGENCY beats SCHEDULED beats CARGO
        System.out.println("=== Scenario 1: strict priority ordering ===");
        scheduler.submit(new FlightRequest(new Flight("CG01", "Cargo-01", Priority.CARGO), Instant.now()));
        scheduler.submit(new FlightRequest(new Flight("SC01", "Scheduled-01", Priority.SCHEDULED), Instant.now()));
        scheduler.submit(new FlightRequest(new Flight("EM01", "Emergency-01", Priority.EMERGENCY), Instant.now()));

        scheduler.dispatchNext(); // EM01
        scheduler.dispatchNext(); // SC01

        // Scenario 2: Cargo has now been starving. Wait past the aging threshold
        // and submit a fresh SCHEDULED flight — aged Cargo should dispatch first.
        System.out.println("\n=== Scenario 2: aging rescues the starved cargo ===");
        Thread.sleep(1500);
        scheduler.submit(new FlightRequest(new Flight("SC02", "Scheduled-02", Priority.SCHEDULED), Instant.now()));

        scheduler.dispatchNext(); // CG01 (aged)
        scheduler.dispatchNext(); // SC02

        scheduler.shutdown();
    }
}
