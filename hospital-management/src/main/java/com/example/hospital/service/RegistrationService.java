package com.example.hospital.service;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.DoctorId;
import com.example.hospital.model.Patient;
import com.example.hospital.model.PatientId;
import com.example.hospital.model.Priority;

import java.util.UUID;

public class RegistrationService {
    private final OPDQueue queue;
    private int sequenceCounter = 0;

    public RegistrationService(OPDQueue queue) {
        this.queue = queue;
    }

    public Patient registerPatient(String name, Priority priority) {
        Patient patient = new Patient(
                new PatientId(UUID.randomUUID().toString()),
                name,
                priority,
                sequenceCounter++
        );
        queue.enqueue(patient);
        System.out.printf("Registered: %s [%s] — arrival sequence=%d%n",
                name, priority, patient.arrivalSequence());
        return patient;
    }

    public DoctorSession registerDoctor(String name) {
        Doctor doctor = new Doctor(new DoctorId(UUID.randomUUID().toString()), name);
        return new DoctorSession(doctor);
    }
}
