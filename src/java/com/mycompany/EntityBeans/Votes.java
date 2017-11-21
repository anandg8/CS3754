/*
 * Created by Osman Balci on 2017.11.21  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
    , @NamedQuery(name = "Votes.findByAction", query = "SELECT v FROM Votes v WHERE v.action = :action")})
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
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "voteId")
    private Comments comments;
    @JoinColumn(name = "comments_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Comments commentsId;
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dish dishId;

    public Votes() {
    }

    public Votes(Integer id) {
        this.id = id;
    }

    public Votes(Integer id, int userId, int action) {
        this.id = id;
        this.userId = userId;
        this.action = action;
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

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Comments getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(Comments commentsId) {
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
