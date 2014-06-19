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

import com.obe.model.Trial;

@ApplicationScoped
public class TrialDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Trial findById(Integer id) {
        return em.find(Trial.class, id);
    }

    public Trial findById(String trialId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Trial> criteria = cb.createQuery(Trial.class);
        Root<Trial> trial = criteria.from(Trial.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Trial).where(cb.equal(Trial.get(Trial_.email), email));
        criteria.select(trial).where(cb.equal(trial.get("trialId"), trialId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Trial> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Trial> criteria = cb.createQuery(Trial.class);
        Root<Trial> trial = criteria.from(Trial.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Trial).orderBy(cb.asc(Trial.get(Trial_.name)));
        criteria.select(trial).orderBy(cb.asc(trial.get("lastName")), cb.asc(trial.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateTrial(Trial newTrial) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Trial> criteria = cb.createQuery(Trial.class);
////    	criteria.
//    	em.persist(newTrial);
//    	
//    }
}
