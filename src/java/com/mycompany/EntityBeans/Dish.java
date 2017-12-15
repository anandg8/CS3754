/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import javax.imageio.ImageIO;
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
    , @NamedQuery(name = "Dish.findByDishName", query = "SELECT d FROM Dish d WHERE d.dishName = :dishName")
    , @NamedQuery(name = "Dish.findByDishPicturePath", query = "SELECT d FROM Dish d WHERE d.dishPicturePath = :dishPicturePath")
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
    @Size(min = 1, max = 255)
    @Column(name = "dish_name")
    private String dishName;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "dish_picture_path")
    private String dishPicturePath;
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
    @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CuisineType cuisineId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "comments_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private CommentsConjunction commentsId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dishId")
    private Collection<Votes> votesCollection;

    /**
     * Manages the Dish Database
     */
    
    public Dish() {
    }

    public Dish(Integer id) {
        this.id = id;
    }

    public Dish(Integer id, String dishName, String description, String dishPicturePath, Date reservationTime, Date mealTime, double cost, int numGuests) {
        this.id = id;
        this.dishName = dishName;
        this.description = description;
        this.dishPicturePath = dishPicturePath;
        this.reservationTime = reservationTime;
        this.mealTime = mealTime;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDishPicturePath() {
        return dishPicturePath;
    }

    /**
     * If the path is not set, set it to the defaultFoodPhoto.png
     * @param dishPicturePath 
     */
    public void setDishPicturePath(String dishPicturePath) {
        if (dishPicturePath.length() == 0)
            this.dishPicturePath = "defaultFoodPhoto.png";
        else {
            if (!testImage(dishPicturePath))
                this.dishPicturePath = dishPicturePath;
            else
                this.dishPicturePath = "defaultFoodPhoto.png";
        }
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

    public int getCost() {
        return (int)cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public CuisineType getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(CuisineType cuisineId) {
        this.cuisineId = cuisineId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public CommentsConjunction getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(CommentsConjunction commentsId) {
        this.commentsId = commentsId;
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
    
    public Boolean testImage(String url){  
        try {  
            BufferedImage image = ImageIO.read(new URL(url));  
            //BufferedImage image = ImageIO.read(new URL("http://someimage.jpg"));  
            return (image != null);
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            System.err.println("URL error with image");  
            e.printStackTrace();
            return false;
        } catch (IOException e) {  
            System.err.println("IO error with image");  
            // TODO Auto-generated catch block  
            e.printStackTrace();
            return false;  
        }
    }
}
