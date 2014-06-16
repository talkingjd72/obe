package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.User;

@Stateless
public class UserRegistration {
	@Inject
    private Logger log;

//    @PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    @Inject
    private Event<User> memberEventSrc;

    public void register(User user) throws Exception {
        log.info("Registering " + user.getIdUser() + " " + user.getLoginName());
        em.persist(user);
        memberEventSrc.fire(user);
    }
}
