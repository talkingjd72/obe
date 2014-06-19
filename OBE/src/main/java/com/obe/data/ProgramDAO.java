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

import com.obe.model.Program;

@ApplicationScoped
public class ProgramDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Program findById(Integer id) {
        return em.find(Program.class, id);
    }

    public Program findById(String programId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Program> criteria = cb.createQuery(Program.class);
        Root<Program> program = criteria.from(Program.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Program).where(cb.equal(Program.get(Program_.email), email));
        criteria.select(program).where(cb.equal(program.get("programId"), programId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Program> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Program> criteria = cb.createQuery(Program.class);
        Root<Program> program = criteria.from(Program.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Program).orderBy(cb.asc(Program.get(Program_.name)));
        criteria.select(program).orderBy(cb.asc(program.get("lastName")), cb.asc(program.get("firstName")));
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
