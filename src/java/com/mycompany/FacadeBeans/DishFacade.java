/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Dish;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    /**
     * Creates a dish. This process is a little bit complicated due to the foreign key checks.
     * Essentially the comments_conjunction needs to be created first before the dish itself.
     * There is no great way to get the insert id so we first create the comment_conjunction,
     * then we fetch the MAX(id) which is the last id inserted. We use that id in the dish
     * and insert that into the database
     * @param dish object to insert
     */
    public void createDish(Dish dish) {
        getEntityManager().createNativeQuery("INSERT INTO comments_conjunction VALUES(DEFAULT, DEFAULT)").executeUpdate();
        Integer b = (Integer)getEntityManager().createNativeQuery("SELECT MAX(id) FROM comments_conjunction").getSingleResult();
        getEntityManager().createNativeQuery("INSERT INTO dish VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, dish.getUserId().getId())
                .setParameter(2, dish.getCuisineId().getId())
                .setParameter(3, dish.getDishName())
                .setParameter(4, dish.getDescription())
                .setParameter(5, dish.getDishPicturePath())
                .setParameter(6, dish.getReservationTime())
                .setParameter(7, dish.getMealTime())
                .setParameter(8, dish.getCost())
                .setParameter(9, dish.getNumGuests())
                .setParameter(10, b)
                .executeUpdate();
    }
    
    /**
     * Get the current number of reservations for a certain dish
     * This information is stored in the 'guest_list' table, which acts as a conjunction table
     * for the users and the dishes
     * @param dishID to search for
     * @return the number of current users signed up for a dish
     */
    public int getCurrentNumReservations(int dishID) {
        return ((Long)getEntityManager().createNativeQuery("SELECT COUNT(*) FROM guest_list WHERE dish_id = ?").setParameter(1, dishID).getSingleResult()).intValue();
    }

    /**
     * Check if the user is a guest (i.e. has already signed up for) for this dish
     * @param userid to check for
     * @param dishid to check for
     * @return true if guest, false otherwise
     */
    public boolean isGuest(int userid, int dishid) {
        int count = ((Long)getEntityManager().createNativeQuery("SELECT COUNT(*) FROM guest_list WHERE user_id = ? AND dish_id = ?")
                .setParameter(1, userid)
                .setParameter(2, dishid)
                .getSingleResult()).intValue();
        return count == 1;        
            
    }

    /**
     * Reserve dishes for a user. This added the user to the guest_list
     * @param dishid to reserve
     * @param userid to add to the guest list
     */
    public void reserveDish(int dishid, int userid) {
        getEntityManager().createNativeQuery("INSERT INTO guest_list VALUES(?, ?, \"\")")
                .setParameter(1, userid)
                .setParameter(2, dishid)
                .executeUpdate();
    }
    
    /**
     * Remove user from the guest list
     * @param dishid to unreserve
     * @param userid to remove from the guest list
     */
    public void unreserveDish(int dishid, int userid) {
        getEntityManager().createNativeQuery("DELETE FROM guest_list WHERE dish_id = ? AND user_id = ?")
                .setParameter(1, dishid)
                .setParameter(2, userid)
                .executeUpdate();
        
    }

    /**
     * Return a list of dishes in the future that are held by a current user's guest subscription
     * This uses a complex SQL query by using a JOIN. We can join the dish id on the guest_list.
     * This creates a larger table where we can query the data more easily. In this case, we create
     * a Dish object using each row retrieved from the database. We know a reservation is current
     * using a comparison in SQL.
     * This has faster execution since this runs directly on SQL. The reverse case would be to pull
     * all the dishes and then manually search through in Java.
     * @param userid to search for
     * @return a list of dishes
     */
    public List<Dish> findCurrentReservationsByUser(int userid) {
        List<Dish> list = getEntityManager().createNativeQuery("SELECT dish.* FROM dish INNER JOIN guest_list ON dish.id = guest_list.dish_id WHERE (guest_list.user_id = ? AND CURRENT_TIMESTAMP < dish.reservation_time)", Dish.class)
                .setParameter(1, userid)
                .getResultList();
        return list;
    }
    
    /**
     * Return all the past reservations of a user. We can fetch this data using the guest list to join on.
     * We know a dish is in the past by comparing the current time to the timestamp in the database
     * @param userid
     * @return 
     */
    public List<Dish> findPastReservationsByUser(int userid) {
        List<Dish> list = getEntityManager().createNativeQuery("SELECT dish.* FROM dish INNER JOIN guest_list ON dish.id = guest_list.dish_id WHERE (guest_list.user_id = ? AND CURRENT_TIMESTAMP > dish.meal_time)", Dish.class)
                .setParameter(1, userid)
                .getResultList();
        return list;
    }
    
    /**
     * Find the list of user dishes
     * @param userID to search for
     * @return a list of all the dishes
     */
    public List<Dish> findCurrentUserDishes(int userID) {
        List<Dish> userDishList = getEntityManager().createNativeQuery("SELECT dish.* FROM dish INNER JOIN user ON dish.user_id = user.id WHERE (user.id = ?)", Dish.class)
                .setParameter(1, userID)
                .getResultList();
        
        return userDishList;
    }
    
    /**
     * Get the comments conjunction id of the dish. This will link the dish to the comments table
     * @param dishid to search for
     * @return the comments table id
     */
    public int getCommentsConjunctionID(int dishid) {
        int id = ((Integer)getEntityManager().createNativeQuery("SELECT comments_id FROM dish WHERE id = ?")
                .setParameter(1, dishid)
                .getSingleResult());
        return id;
    }
    
}
