/*
 * Created by Ananthavel Guruswamy on 2017.12.12  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.DishPhoto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Anand
 */
@Stateless
public class DishPhotoFacade extends AbstractFacade<DishPhoto> {

    @PersistenceContext(unitName = "CookToSharePU")
    private EntityManager em;

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /* 
    This constructor method invokes the parent abstract class AbstractFacade.java's
    constructor method AbstractFacade, which in turn initializes its entityClass instance
    variable with the Photo class object reference returned by the Photo.class.
     */
    public DishPhotoFacade() {
        super(DishPhoto.class);
    }
    
    /*
    ====================================================
    The following method is added to the generated code.
    ====================================================
     */
    /** @param dishID
     *  @return list of dish photos.
     */
    public List<DishPhoto> findPhotosByDishID(Integer dishID) {

        return (List<DishPhoto>) em.createNamedQuery("DishPhoto.findPhotosByDishID")
                .setParameter("dish_id", dishID)
                .getResultList();
    }

    /* The following methods are inherited from the parent AbstractFacade class:
    
        create
        edit
        find
        findAll
        remove
     */
    
}
