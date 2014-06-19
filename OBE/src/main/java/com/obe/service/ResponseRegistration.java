package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.ResponseModel;

@Stateless
public class ResponseRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<ResponseModel> memberEventSrc;

    public void register(ResponseModel response) throws Exception {
        log.info("Registering " + response.getIdResponse() + " " +
        		response.getName());
        em.persist(response);
        memberEventSrc.fire(response);
    }
}
