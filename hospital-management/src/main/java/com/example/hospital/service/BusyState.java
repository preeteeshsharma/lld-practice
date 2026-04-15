package com.example.hospital.service;

import com.example.hospital.model.Patient;

public class BusyState implements DoctorState {
    public static final BusyState INSTANCE = new BusyState();

    private BusyState() {
    }

    @Override
    public void callNext(DoctorSession session, OPDQueue queue) {
        throw new IllegalStateException("Dr. " + session.getDoctor().name() + " is busy");
    }

    @Override
    public void completeConsultation(DoctorSession session) {
        Patient current = session.getCurrentPatient();
        if (current == null) {
            // shouldn't happen in normal flow — guard against corrupt state
            session.setState(AvailableState.INSTANCE);
            return;
        }
        System.out.printf("Dr. %s completed consultation with: %s%n",
                session.getDoctor().name(), current.name());
        session.clearPatient();
        session.setState(AvailableState.INSTANCE);
    }
}
