package com.example.hospital.service;

import com.example.hospital.model.Consultation;
import com.example.hospital.model.Doctor;

public final class DoctorSession {
    private final Doctor doctor;
    private DoctorState state;
    private Consultation currentConsultation;

    public DoctorSession(Doctor doctor) {
        this.doctor = doctor;
        this.state = AvailableState.INSTANCE;
    }

    void setState(DoctorState state) { this.state = state; }
    void assignConsultation(Consultation c) { this.currentConsultation = c; }
    void clearConsultation() { this.currentConsultation = null; }

    public Doctor getDoctor() { return doctor; }
    public Consultation getCurrentConsultation() { return currentConsultation; }
    public boolean isAvailable() { return state instanceof AvailableState; }

    public void start(Consultation consultation) { state.start(this, consultation); }
    public Consultation complete() { return state.complete(this); }
}
