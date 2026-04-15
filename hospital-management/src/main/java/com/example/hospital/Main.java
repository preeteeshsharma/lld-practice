package com.example.hospital;

import com.example.hospital.model.Consultation;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Priority;
import com.example.hospital.model.TreatmentRecord;
import com.example.hospital.service.ConsultationService;
import com.example.hospital.service.DoctorSession;
import com.example.hospital.service.OPDQueue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        OPDQueue queue = new OPDQueue();
        ConsultationService service = new ConsultationService(queue);

        // Reception-like: register patients directly (no dedicated Reception class for time)
        queue.enqueue(new Patient(UUID.randomUUID().toString(), "Alice", Priority.GENERAL, Instant.now()));
        queue.enqueue(new Patient(UUID.randomUUID().toString(), "Bob", Priority.GENERAL, Instant.now()));
        queue.enqueue(new Patient(UUID.randomUUID().toString(), "Charlie", Priority.EMERGENCY, Instant.now()));

        // Registry-like: create doctor + session directly
        DoctorSession smith = new DoctorSession(new Doctor(UUID.randomUUID().toString(), "Smith"));
        List<DoctorSession> sessions = List.of(smith);

        // Scenario 1: EMERGENCY served first, then GENERAL in FIFO order
        System.out.println("=== Scenario 1: EMERGENCY before GENERAL ===");
        dispatchAndClose(service, sessions, smith, "Acute pain", "Painkillers");
        dispatchAndClose(service, sessions, smith, "Common cold", "Rest");
        dispatchAndClose(service, sessions, smith, "Mild fever", "Paracetamol");

        // Scenario 2: empty queue
        System.out.println("\n=== Scenario 2: Empty queue ===");
        Optional<Consultation> none = service.dispatchNext(sessions);
        System.out.println("Dispatched: " + (none.isPresent() ? "unexpected" : "nothing — queue empty"));

        // Scenario 3: busy doctor blocks second dispatch until complete
        System.out.println("\n=== Scenario 3: Busy doctor blocked ===");
        queue.enqueue(new Patient(UUID.randomUUID().toString(), "Dave", Priority.GENERAL, Instant.now()));
        queue.enqueue(new Patient(UUID.randomUUID().toString(), "Eve", Priority.GENERAL, Instant.now()));

        service.dispatchNext(sessions);
        Optional<Consultation> blocked = service.dispatchNext(sessions);
        System.out.println("Second dispatch while busy: " + (blocked.isEmpty() ? "blocked" : "unexpected"));

        service.completeConsultation(smith, "Headache", "Ibuprofen");
        Optional<Consultation> eve = service.dispatchNext(sessions);
        System.out.println("Dispatched Eve after Smith freed: " + eve.isPresent());
        service.completeConsultation(smith, "Sore throat", "Lozenges");
    }

    private static void dispatchAndClose(ConsultationService service,
                                         List<DoctorSession> sessions,
                                         DoctorSession session,
                                         String diagnosis,
                                         String prescription) {
        Optional<Consultation> c = service.dispatchNext(sessions);
        if (c.isEmpty()) { System.out.println("Nothing to dispatch"); return; }
        System.out.println("Started: patient=" + c.get().patientId() + " doctor=" + session.getDoctor().name());
        TreatmentRecord r = service.completeConsultation(session, diagnosis, prescription);
        System.out.println("Closed: consultation=" + r.consultationId() + " dx=" + r.diagnosis());
    }
}
