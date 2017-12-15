/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amitd
 */
@Entity
@Table(name = "votes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Votes.findAll", query = "SELECT v FROM Votes v")
    , @NamedQuery(name = "Votes.findById", query = "SELECT v FROM Votes v WHERE v.id = :id")
    , @NamedQuery(name = "Votes.findByUserId", query = "SELECT v FROM Votes v WHERE v.userId = :userId")
    , @NamedQuery(name = "Votes.findByAction", query = "SELECT v FROM Votes v WHERE v.action = :action")
    , @NamedQuery(name = "Votes.findByCommentsId", query = "SELECT v FROM Votes v WHERE v.commentsId = :commentsId")})
public class Votes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action")
    private int action;
    @Basic(optional = false)
    @NotNull
    @Column(name = "comments_id")
    private int commentsId;
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dish dishId;

    /**
     * Controls the vote table
     * The vote tables tracks which user voted for which comment
     * This prevents a user from 'brigading' or voting an infinite number of
     * times for a specific comment
     */
    
    public Votes() {
    }

    public Votes(Integer id) {
        this.id = id;
    }

    public Votes(Integer id, int userId, int action, int commentsId) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.commentsId = commentsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(int commentsId) {
        this.commentsId = commentsId;
    }

    public Dish getDishId() {
        return dishId;
    }

    public void setDishId(Dish dishId) {
        this.dishId = dishId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Votes)) {
            return false;
        }
        Votes other = (Votes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.Votes[ id=" + id + " ]";
    }
    
}
