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

import com.obe.model.Reinforcement;

@ApplicationScoped
public class ReinforcementDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Reinforcement findById(Integer id) {
        return em.find(Reinforcement.class, id);
    }

    public Reinforcement findById(String reinforcementId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Reinforcement> criteria = cb.createQuery(Reinforcement.class);
        Root<Reinforcement> reinforcement = criteria.from(Reinforcement.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Reinforcement).where(cb.equal(Reinforcement.get(Reinforcement_.email), email));
        criteria.select(reinforcement).where(cb.equal(reinforcement.get("reinforcementId"), reinforcementId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Reinforcement> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Reinforcement> criteria = cb.createQuery(Reinforcement.class);
        Root<Reinforcement> reinforcement = criteria.from(Reinforcement.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Reinforcement).orderBy(cb.asc(Reinforcement.get(Reinforcement_.name)));
        criteria.select(reinforcement).orderBy(cb.asc(reinforcement.get("lastName")), cb.asc(reinforcement.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateReinforcement(Reinforcement newReinforcement) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Reinforcement> criteria = cb.createQuery(Reinforcement.class);
////    	criteria.
//    	em.persist(newReinforcement);
//    	
//    }
}
