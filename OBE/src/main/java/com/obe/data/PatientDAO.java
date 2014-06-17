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

import com.obe.model.Patient;

@ApplicationScoped
public class PatientDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public Patient findById(Integer id) {
        return em.find(Patient.class, id);
    }

    public Patient findById(String patientId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = cb.createQuery(Patient.class);
        Root<Patient> patient = criteria.from(Patient.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Patient).where(cb.equal(Patient.get(Patient_.email), email));
        criteria.select(patient).where(cb.equal(patient.get("patientId"), patientId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Patient> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Patient> criteria = cb.createQuery(Patient.class);
        Root<Patient> patient = criteria.from(Patient.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Patient).orderBy(cb.asc(Patient.get(Patient_.name)));
        criteria.select(patient).orderBy(cb.asc(patient.get("lastName")), cb.asc(patient.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
    
}
