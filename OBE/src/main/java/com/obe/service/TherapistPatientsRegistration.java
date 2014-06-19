package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.TherapistPatients;

@Stateless
public class TherapistPatientsRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<TherapistPatients> memberEventSrc;

    public void register(TherapistPatients therapistPatients) throws Exception {
        log.info("Registering " + therapistPatients.getIdTherapistPatients() + " " +
        		therapistPatients.getIdTherapist() + " " + therapistPatients.getIdPatient());
        em.persist(therapistPatients);
        memberEventSrc.fire(therapistPatients);
    }
}
