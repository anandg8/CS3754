/*
 * Created by Osman Balci on 2017.11.21  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Dish;
import java.math.BigInteger;
import java.sql.Statement;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author amitd
 */
@Stateless
public class DishFacade extends AbstractFacade<Dish> {

    @PersistenceContext(unitName = "CookToSharePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DishFacade() {
        super(Dish.class);
    }
    
    public void createDish(Dish dish) {
        getEntityManager().createNativeQuery("INSERT INTO comments_conjunction VALUES(DEFAULT, DEFAULT)").executeUpdate();
        Integer b = (Integer)getEntityManager().createNativeQuery("SELECT MAX(id) FROM comments_conjunction").getSingleResult();
        getEntityManager().createNativeQuery("INSERT INTO dish VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, dish.getUserId().getId())
                .setParameter(2, dish.getCuisineId().getId())
                .setParameter(3, dish.getDishName())
                .setParameter(4, dish.getDescription())
                .setParameter(5, "")
                .setParameter(6, dish.getReservationTime())
                .setParameter(7, dish.getMealTime())
                .setParameter(8, dish.getCost())
                .setParameter(9, dish.getNumGuests())
                .setParameter(10, b)
                .executeUpdate();
    }
    
}
