package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Program;

@Stateless
public class ProgramRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Program> memberEventSrc;

    public void register(Program program) throws Exception {
        log.info("Registering " + program.getIdProgram() + " " +
        		program.getName());
        em.persist(program);
        memberEventSrc.fire(program);
    }
}
