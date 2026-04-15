package com.example.airport.service;

import com.example.airport.model.FlightRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// Single-runway scheduler.
//
//  Concurrency primitives:
//    - PriorityBlockingQueue: thread-safe priority access. Multiple threads can
//      submit; dispatch thread(s) can take() concurrently.
//    - ReentrantLock on the runway: only one flight uses the runway at a time,
//      even if multiple dispatchers call dispatchNext() simultaneously.
//    - ScheduledExecutorService: aging tick that bumps priority of starved requests.
//
//  Aging approach:
//    PriorityBlockingQueue does not re-heapify on element mutation, so we drain
//    the queue, bump priorities of requests that have waited beyond the threshold,
//    and re-offer. Brute force O(n) per tick — acceptable for LLD scope.
public class RunwayScheduler {
    private final PriorityBlockingQueue<FlightRequest> queue = new PriorityBlockingQueue<>(
            11,
            Comparator.comparingInt(FlightRequest::effectiveRank)
                      .thenComparing(FlightRequest::requestedAt)
    );
    private final ReentrantLock runwayLock = new ReentrantLock();
    private final ScheduledExecutorService agingExecutor;
    private final Duration agingThreshold;

    public RunwayScheduler(Duration agingThreshold, long agingIntervalMs) {
        this.agingThreshold = agingThreshold;
        this.agingExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "runway-aging");
            t.setDaemon(true);
            return t;
        });
        agingExecutor.scheduleAtFixedRate(this::ageWaiters, agingIntervalMs, agingIntervalMs, TimeUnit.MILLISECONDS);
    }

    public void submit(FlightRequest request) {
        queue.offer(request);
        System.out.printf("[submit] %s rank=%d%n", request.flight().callsign(), request.effectiveRank());
    }

    // Blocks until a flight is available. Acquires the runway lock, simulates the
    // operation, then releases. The lock guarantees mutual exclusion on the runway
    // even if callers race.
    public FlightRequest dispatchNext() throws InterruptedException {
        FlightRequest next = queue.take();
        runwayLock.lock();
        try {
            System.out.printf("[runway] cleared: %s (rank=%d)%n",
                    next.flight().callsign(), next.effectiveRank());
            Thread.sleep(50); // simulate runway occupation
            return next;
        } finally {
            runwayLock.unlock();
        }
    }

    private void ageWaiters() {
        List<FlightRequest> snapshot = new ArrayList<>();
        queue.drainTo(snapshot);
        Instant now = Instant.now();
        for (FlightRequest r : snapshot) {
            Duration waited = Duration.between(r.requestedAt(), now);
            if (waited.compareTo(agingThreshold) >= 0 && r.effectiveRank() > 0) {
                if (r.bumpPriority()) {
                    System.out.printf("[aging] %s bumped to rank=%d after %dms%n",
                            r.flight().callsign(), r.effectiveRank(), waited.toMillis());
                }
            }
            queue.offer(r);
        }
    }

    public void shutdown() {
        agingExecutor.shutdown();
    }
}
