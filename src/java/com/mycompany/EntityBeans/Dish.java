/*
 * Created by Osman Balci on 2017.11.21  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amitd
 */
@Entity
@Table(name = "dish")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dish.findAll", query = "SELECT d FROM Dish d")
    , @NamedQuery(name = "Dish.findById", query = "SELECT d FROM Dish d WHERE d.id = :id")
    , @NamedQuery(name = "Dish.findByReservationTime", query = "SELECT d FROM Dish d WHERE d.reservationTime = :reservationTime")
    , @NamedQuery(name = "Dish.findByMealTime", query = "SELECT d FROM Dish d WHERE d.mealTime = :mealTime")
    , @NamedQuery(name = "Dish.findByCost", query = "SELECT d FROM Dish d WHERE d.cost = :cost")
    , @NamedQuery(name = "Dish.findByNumGuests", query = "SELECT d FROM Dish d WHERE d.numGuests = :numGuests")})
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reservation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "meal_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mealTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost")
    private double cost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_guests")
    private int numGuests;
    @JoinColumn(name = "comments_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Comments commentsId;
    @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CuisineTable cuisineId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dishId")
    private Collection<Votes> votesCollection;

    public Dish() {
    }

    public Dish(Integer id) {
        this.id = id;
    }

    public Dish(Integer id, String description, Date reservationTime, Date mealTime, double cost, int numGuests) {
        this.id = id;
        this.description = description;
        this.reservationTime = reservationTime;
        this.mealTime = mealTime;
        this.cost = cost;
        this.numGuests = numGuests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Date getMealTime() {
        return mealTime;
    }

    public void setMealTime(Date mealTime) {
        this.mealTime = mealTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public Comments getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(Comments commentsId) {
        this.commentsId = commentsId;
    }

    public CuisineTable getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(CuisineTable cuisineId) {
        this.cuisineId = cuisineId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<Votes> getVotesCollection() {
        return votesCollection;
    }

    public void setVotesCollection(Collection<Votes> votesCollection) {
        this.votesCollection = votesCollection;
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
        if (!(object instanceof Dish)) {
            return false;
        }
        Dish other = (Dish) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.Dish[ id=" + id + " ]";
    }
    
}
