/*
 * Created by Osman Balci on 2017.11.21  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.User;
import java.math.BigDecimal;
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
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "CookToSharePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
        /**
     * @param id is the Primary Key of the User entity in a table row in the CloudDriveDB database.
     * @return object reference of the User entity whose primary key is id
     */
    public User getUser(int id) {
        
        // The find method is inherited from the parent AbstractFacade class
        return em.find(User.class, id);
    }

    /**
     * @param username is the username attribute (column) value of the user
     * @return object reference of the User entity whose username is username
     */
    public User findByUsername(String username) {
        if (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                .setParameter("username", username)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (User) (em.createQuery("SELECT c FROM User c WHERE c.username = :username")
                    .setParameter("username", username)
                    .getSingleResult());
        }
    }

    /**
     * Deletes the User entity whose primary key is id
     * @param id is the Primary Key of the User entity in a table row in the CloudDriveDB database.
     */
    public void deleteUser(int id) {
        
        // The find method is inherited from the parent AbstractFacade class
        User user = em.find(User.class, id);
        
        // The remove method is inherited from the parent AbstractFacade class
        em.remove(user); 
    }

    public int getCredits(int id) {
        Query q = em.createNativeQuery("SELECT credits_available FROM user_credits WHERE user_id = ?");
        q.setParameter(1, "" + id);
        Integer d = (Integer) q.getSingleResult();
        return d.intValue();

    }

    public void createCreditAccount(Integer id) {
        em.createNativeQuery("INSERT INTO user_credits VALUES(DEFAULT, ?, 0)").setParameter(1, id).executeUpdate();
    }

    public void setCredits(int credit, int userid) {
        em.createNativeQuery("UPDATE user_credits SET credits_available = ? WHERE user_id = ?").setParameter(1, credit).setParameter(2, userid).executeUpdate();
    }

    public void creditAccount(int userid, int addition) {
        setCredits(getCredits(userid) + addition, userid);
    }

    public void withdrawAccount(int from, int cost) {
        setCredits(getCredits(from) - cost, from);
    }
    
}
