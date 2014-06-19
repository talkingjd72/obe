package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Patient;

@Stateless
public class PatientRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Patient> memberEventSrc;

    public void register(Patient patient) throws Exception {
        log.info("Registering " + patient.getIdPatient() + " " +
        		patient.getFirstName() + " " + patient.getLastName());
        em.persist(patient);
        memberEventSrc.fire(patient);
    }
}
