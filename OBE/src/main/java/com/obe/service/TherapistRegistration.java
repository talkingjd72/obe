package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Therapist;

@Stateless
public class TherapistRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Therapist> memberEventSrc;

    public void register(Therapist therapist) throws Exception {
        log.info("Registering " + therapist.getIdTherapist() + " " +
        		therapist.getFirstName() + " " + therapist.getLastName());
        em.persist(therapist);
        memberEventSrc.fire(therapist);
    }
}
