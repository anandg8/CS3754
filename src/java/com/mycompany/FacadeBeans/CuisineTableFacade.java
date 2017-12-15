/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.CuisineType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author amitd
 */
@Stateless
public class CuisineTableFacade extends AbstractFacade<CuisineType> {

    @PersistenceContext(unitName = "CookToSharePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CuisineTableFacade() {
        super(CuisineType.class);
    }
    
}
