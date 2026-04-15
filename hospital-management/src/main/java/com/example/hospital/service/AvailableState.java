package com.example.hospital.service;

import com.example.hospital.model.Patient;

import java.util.Optional;

public class AvailableState implements DoctorState {
    public static final AvailableState INSTANCE = new AvailableState();

    private AvailableState() {
    }

    @Override
    public void callNext(DoctorSession session, OPDQueue queue) {
        Optional<Patient> next = queue.poll();
        if (next.isEmpty()) {
            System.out.printf("Dr. %s: queue is empty%n", session.getDoctor().name());
            return;
        }
        Patient patient = next.get();
        session.assignPatient(patient);
        session.setState(BusyState.INSTANCE);
        System.out.printf("Dr. %s is now seeing: %s [%s]%n",
                session.getDoctor().name(), patient.name(), patient.priority());
    }

    @Override
    public void completeConsultation(DoctorSession session) {
        // no-op — already available
    }
}
