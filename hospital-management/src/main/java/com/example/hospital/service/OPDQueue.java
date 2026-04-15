package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class OPDQueue {
    // rank() is explicit — not ordinal(), which breaks if enum order changes.
    // FIFO tiebreak within same priority: lower arrivalSequence served first.
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.priority().rank())
                      .thenComparingInt(Patient::arrivalSequence)
    );

    public void enqueue(Patient patient) {
        queue.offer(patient);
    }

    public Optional<Patient> poll() {
        return Optional.ofNullable(queue.poll());
    }

    public int size() {
        return queue.size();
    }
}
