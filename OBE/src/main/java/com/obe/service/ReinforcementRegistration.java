package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Reinforcement;

@Stateless
public class ReinforcementRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Reinforcement> memberEventSrc;

    public void register(Reinforcement reinforcement) throws Exception {
        log.info("Registering " + reinforcement.getIdReinforcement() + " " +
        		reinforcement.getName());
        em.persist(reinforcement);
        memberEventSrc.fire(reinforcement);
    }
}
