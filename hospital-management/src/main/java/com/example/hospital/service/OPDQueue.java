package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class OPDQueue {
    // rank() not ordinal() — safe against enum reorder.
    // Tiebreak: earlier registeredAt first (FIFO within a priority).
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.priority().rank())
                    .thenComparing(Patient::registeredAt)
    );

    public void enqueue(Patient patient) {
        queue.offer(patient);
    }

    public Optional<Patient> peek() {
        return Optional.ofNullable(queue.peek());
    }

    public Optional<Patient> poll() {
        return Optional.ofNullable(queue.poll());
    }

    public int size() {
        return queue.size();
    }
}
