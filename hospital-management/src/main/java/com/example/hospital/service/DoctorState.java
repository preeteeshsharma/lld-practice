package com.example.hospital.service;

public interface DoctorState {
    void callNext(DoctorSession session, OPDQueue queue);
    void completeConsultation(DoctorSession session);
}
