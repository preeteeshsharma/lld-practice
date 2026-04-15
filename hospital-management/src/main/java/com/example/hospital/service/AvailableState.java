package com.example.hospital.service;

import com.example.hospital.model.Consultation;

public final class AvailableState implements DoctorState {
    public static final AvailableState INSTANCE = new AvailableState();

    private AvailableState() {}

    @Override
    public void start(DoctorSession session, Consultation consultation) {
        session.assignConsultation(consultation);
        session.setState(BusyState.INSTANCE);
    }

    @Override
    public Consultation complete(DoctorSession session) {
        throw new IllegalStateException("Dr. " + session.getDoctor().name() + " has no active consultation");
    }
}
