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

import com.obe.model.Stimulus;

@ApplicationScoped
public class StimulusDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Stimulus findById(Integer id) {
        return em.find(Stimulus.class, id);
    }

    public Stimulus findById(String stimulusId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Stimulus> criteria = cb.createQuery(Stimulus.class);
        Root<Stimulus> stimulus = criteria.from(Stimulus.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Stimulus).where(cb.equal(Stimulus.get(Stimulus_.email), email));
        criteria.select(stimulus).where(cb.equal(stimulus.get("stimulusId"), stimulusId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Stimulus> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Stimulus> criteria = cb.createQuery(Stimulus.class);
        Root<Stimulus> stimulus = criteria.from(Stimulus.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Stimulus).orderBy(cb.asc(Stimulus.get(Stimulus_.name)));
        criteria.select(stimulus).orderBy(cb.asc(stimulus.get("lastName")), cb.asc(stimulus.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateProgram(Program newProgram) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Program> criteria = cb.createQuery(Program.class);
////    	criteria.
//    	em.persist(newProgram);
//    	
//    }
}
