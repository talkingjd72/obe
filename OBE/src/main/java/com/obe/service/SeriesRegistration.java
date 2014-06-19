package com.obe.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.obe.model.Series;

@Stateless
public class SeriesRegistration {
	@Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Series> memberEventSrc;

    public void register(Series series) throws Exception {
        log.info("Registering " + series.getIdSeries() + " " +
        		series.getType());
        em.persist(series);
        memberEventSrc.fire(series);
    }
}
