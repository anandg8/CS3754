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
    
    public int getCurrentNumReservations(int dishID) {
        return ((Long)getEntityManager().createNativeQuery("SELECT COUNT(*) FROM guest_list WHERE dish_id = ?").setParameter(1, dishID).getSingleResult()).intValue();
    }

    public boolean isGuest(int userid, int dishid) {
        int count = ((Long)getEntityManager().createNativeQuery("SELECT COUNT(*) FROM guest_list WHERE user_id = ? AND dish_id = ?")
                .setParameter(1, userid)
                .setParameter(2, dishid)
                .getSingleResult()).intValue();
        System.out.println("count is: " + count + "\n userid is:" +userid + "\n dish is: " + dishid);
        return count == 1;        
            
    }

    public void reserveDish(int dishid, int userid) {
        getEntityManager().createNativeQuery("INSERT INTO guest_list VALUES(?, ?, \"\")")
                .setParameter(1, userid)
                .setParameter(2, dishid)
                .executeUpdate();
    }
    
    public void unreserveDish(int dishid, int userid) {
        getEntityManager().createNativeQuery("DELETE FROM guest_list WHERE dish_id = ? AND user_id = ?")
                .setParameter(1, dishid)
                .setParameter(2, userid)
                .executeUpdate();
        
    }

    public List<Dish> findCurrentReservationsByUser(int userid) {
        List<Dish> list = getEntityManager().createNativeQuery("SELECT dish.* FROM dish INNER JOIN guest_list ON dish.id = guest_list.dish_id WHERE (guest_list.user_id = ? AND CURRENT_TIMESTAMP < dish.reservation_time)", Dish.class)
                .setParameter(1, userid)
                .getResultList();
        return list;
    }
    
    public List<Dish> findPastReservationsByUser(int userid) {
        System.out.println("userid: " + userid);
        List<Dish> list = getEntityManager().createNativeQuery("SELECT dish.* FROM dish INNER JOIN guest_list ON dish.id = guest_list.dish_id WHERE (guest_list.user_id = ? AND CURRENT_TIMESTAMP > dish.meal_time)", Dish.class)
                .setParameter(1, userid)
                .getResultList();
        return list;
    }
    
}
