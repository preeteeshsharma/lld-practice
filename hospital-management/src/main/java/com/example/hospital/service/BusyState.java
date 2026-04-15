package com.example.hospital.service;

import com.example.hospital.model.Consultation;

public final class BusyState implements DoctorState {
    public static final BusyState INSTANCE = new BusyState();

    private BusyState() {}

    @Override
    public void start(DoctorSession session, Consultation consultation) {
        throw new IllegalStateException("Dr. " + session.getDoctor().name() + " is already busy");
    }

    @Override
    public Consultation complete(DoctorSession session) {
        Consultation closed = session.getCurrentConsultation();
        session.clearConsultation();
        session.setState(AvailableState.INSTANCE);
        return closed;
    }
}
