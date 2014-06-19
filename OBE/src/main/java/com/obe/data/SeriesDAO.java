/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obe.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import com.obe.model.Series;

@ApplicationScoped
public class SeriesDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Series findById(Integer id) {
        return em.find(Series.class, id);
    }

    public Series findById(String seriesId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Series> criteria = cb.createQuery(Series.class);
        Root<Series> series = criteria.from(Series.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Series).where(cb.equal(Series.get(Series_.email), email));
        criteria.select(series).where(cb.equal(series.get("seriesId"), seriesId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Series> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Series> criteria = cb.createQuery(Series.class);
        Root<Series> series = criteria.from(Series.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Series).orderBy(cb.asc(Series.get(Series_.name)));
        criteria.select(series).orderBy(cb.asc(series.get("lastName")), cb.asc(series.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateSeries(Series newSeries) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Series> criteria = cb.createQuery(Series.class);
////    	criteria.
//    	em.persist(newSeries);
//    	
//    }
}
