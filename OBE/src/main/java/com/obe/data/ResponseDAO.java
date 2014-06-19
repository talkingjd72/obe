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

import com.obe.model.ResponseModel;

@ApplicationScoped
public class ResponseDAO {

//	@PersistenceContext(unitName="primary")
    @Inject
    private EntityManager em;

    public ResponseModel findById(Integer id) {
        return em.find(ResponseModel.class, id);
    }

    public ResponseModel findById(String responseId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResponseModel> criteria = cb.createQuery(ResponseModel.class);
        Root<ResponseModel> response = criteria.from(ResponseModel.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Response).where(cb.equal(Response.get(Response_.email), email));
        criteria.select(response).where(cb.equal(response.get("responseId"), responseId));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<ResponseModel> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResponseModel> criteria = cb.createQuery(ResponseModel.class);
        Root<ResponseModel> response = criteria.from(ResponseModel.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Response).orderBy(cb.asc(Response.get(Response_.name)));
        criteria.select(response).orderBy(cb.asc(response.get("lastName")), cb.asc(response.get("firstName")));
        return em.createQuery(criteria).getResultList();
    }
    
//    public void updateResponse(Response newResponse) {
////    	CriteriaBuilder cb = em.getCriteriaBuilder();
////    	CriteriaQuery<Response> criteria = cb.createQuery(Response.class);
////    	criteria.
//    	em.persist(newResponse);
//    	
//    }
}
