package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Trial;

@Stateless
public class TrialRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Trial> memberEventSrc;

    public void register(Trial trial) throws Exception {
        log.info("Registering " + trial.getIdTrial() + " " +
        		trial.getName());
        em.persist(trial);
        memberEventSrc.fire(trial);
    }
}
