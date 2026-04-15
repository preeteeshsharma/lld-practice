package com.example.hospital.service;

public class ConsultationService {
    private final OPDQueue queue;

    public ConsultationService(OPDQueue queue) {
        this.queue = queue;
    }

    public void callNext(DoctorSession session) {
        session.callNext(queue);
    }

    public void completeConsultation(DoctorSession session) {
        session.completeConsultation();
    }
}
