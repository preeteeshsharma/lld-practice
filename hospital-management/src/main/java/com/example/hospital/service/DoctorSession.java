package com.example.hospital.service;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;

public class DoctorSession {
    private final Doctor doctor;
    private DoctorState state;
    private Patient currentPatient;

    public DoctorSession(Doctor doctor) {
        this.doctor = doctor;
        this.state = AvailableState.INSTANCE;
    }

    // package-private — only state classes within this package should drive transitions
    void setState(DoctorState state) {
        this.state = state;
    }

    // package-private — called only by state classes
    void assignPatient(Patient patient) {
        this.currentPatient = patient;
    }

    // package-private — called only by state classes
    void clearPatient() {
        this.currentPatient = null;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    // delegates to state
    public void callNext(OPDQueue queue) {
        state.callNext(this, queue);
    }

    public void completeConsultation() {
        state.completeConsultation(this);
    }
}
