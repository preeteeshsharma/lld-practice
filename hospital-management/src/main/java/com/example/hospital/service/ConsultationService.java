package com.example.hospital.service;

import com.example.hospital.model.Consultation;
import com.example.hospital.model.Patient;
import com.example.hospital.model.TreatmentRecord;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Orchestrates: peek queue -> find available doctor -> create Consultation
//               -> drive state transition -> on close, build TreatmentRecord.
public class ConsultationService {
    private final OPDQueue queue;

    public ConsultationService(OPDQueue queue) {
        this.queue = queue;
    }

    public Optional<Consultation> dispatchNext(List<DoctorSession> sessions) {
        Optional<Patient> head = queue.peek();
        if (head.isEmpty()) return Optional.empty();

        Optional<DoctorSession> available = sessions.stream()
                .filter(DoctorSession::isAvailable)
                .findFirst();
        if (available.isEmpty()) return Optional.empty();

        queue.poll();
        DoctorSession session = available.get();
        Consultation consultation = new Consultation(
                UUID.randomUUID().toString(),
                head.get().id(),
                session.getDoctor().id(),
                Instant.now()
        );
        session.start(consultation);
        return Optional.of(consultation);
    }

    public TreatmentRecord completeConsultation(DoctorSession session, String diagnosis, String prescription) {
        Consultation closed = session.complete();
        return new TreatmentRecord(closed.id(), diagnosis, prescription, Instant.now());
    }
}
