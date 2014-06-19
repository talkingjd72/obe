package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Stimulus;

@Stateless
public class StimulusRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Stimulus> memberEventSrc;

    public void register(Stimulus stimulus) throws Exception {
        log.info("Registering " + stimulus.getIdStimulus() + " " +
        		stimulus.getName());
        em.persist(stimulus);
        memberEventSrc.fire(stimulus);
    }
}
