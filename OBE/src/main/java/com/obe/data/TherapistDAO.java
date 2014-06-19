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

import com.obe.model.Therapist;

@ApplicationScoped
public class TherapistDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Therapist findById(Integer id) {
        return em.find(Therapist.class, id);
    }

    public Therapist findById(String therapistId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Therapist> criteria = cb.createQuery(Therapist.class);
        Root<Therapist> therapist = criteria.from(Therapist.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Therapist).where(cb.equal(Therapist.get(Therapist_.email), email));
        criteria.select(therapist).where(cb.equal(therapist.get("therapistId"), therapistId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Therapist> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Therapist> criteria = cb.createQuery(Therapist.class);
        Root<Therapist> therapist = criteria.from(Therapist.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Therapist).orderBy(cb.asc(Therapist.get(Therapist_.name)));
        criteria.select(therapist).orderBy(cb.asc(therapist.get("lastName")), cb.asc(therapist.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateTherapist(Therapist newTherapist) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Therapist> criteria = cb.createQuery(Therapist.class);
////    	criteria.
//    	em.persist(newTherapist);
//    	
//    }
}
