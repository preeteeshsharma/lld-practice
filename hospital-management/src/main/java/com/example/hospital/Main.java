package com.example.hospital;

import com.example.hospital.model.Priority;
import com.example.hospital.service.ConsultationService;
import com.example.hospital.service.DoctorSession;
import com.example.hospital.service.OPDQueue;
import com.example.hospital.service.RegistrationService;

public class Main {
    public static void main(String[] args) {
        // Scenario 1: EMERGENCY jumps ahead of GENERAL patients
        System.out.println("=== Scenario 1: EMERGENCY before GENERAL ===");
        OPDQueue queue1 = new OPDQueue();
        RegistrationService reg1 = new RegistrationService(queue1);
        ConsultationService consult1 = new ConsultationService(queue1);

        reg1.registerPatient("Alice", Priority.GENERAL);
        reg1.registerPatient("Bob", Priority.GENERAL);
        reg1.registerPatient("Charlie", Priority.EMERGENCY);  // arrives last, served first

        DoctorSession drSmith = reg1.registerDoctor("Smith");
        consult1.callNext(drSmith);       // Charlie (EMERGENCY)
        consult1.completeConsultation(drSmith);
        consult1.callNext(drSmith);       // Alice (GENERAL, arrived first)
        consult1.completeConsultation(drSmith);
        consult1.callNext(drSmith);       // Bob (GENERAL)
        consult1.completeConsultation(drSmith);

        // Scenario 2: Empty queue handled gracefully
        System.out.println("\n=== Scenario 2: Empty queue ===");
        consult1.callNext(drSmith);

        // Scenario 3: Busy doctor cannot call next
        System.out.println("\n=== Scenario 3: Busy doctor blocked ===");
        OPDQueue queue2 = new OPDQueue();
        RegistrationService reg2 = new RegistrationService(queue2);
        ConsultationService consult2 = new ConsultationService(queue2);

        reg2.registerPatient("Dave", Priority.GENERAL);
        reg2.registerPatient("Eve", Priority.GENERAL);

        DoctorSession drJones = reg2.registerDoctor("Jones");
        consult2.callNext(drJones);  // Dave
        try {
            consult2.callNext(drJones);  // should throw — already busy
        } catch (IllegalStateException e) {
            System.out.println("Blocked: " + e.getMessage());
        }
        consult2.completeConsultation(drJones);
        consult2.callNext(drJones);  // Eve
    }
}
