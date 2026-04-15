package com.example.hospital.service;

import com.example.hospital.model.Consultation;

public sealed interface DoctorState permits AvailableState, BusyState {
    void start(DoctorSession session, Consultation consultation);
    Consultation complete(DoctorSession session);
}
