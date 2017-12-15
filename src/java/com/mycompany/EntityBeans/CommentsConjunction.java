/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amitd
 */
@Entity
@Table(name = "comments_conjunction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommentsConjunction.findAll", query = "SELECT c FROM CommentsConjunction c")
    , @NamedQuery(name = "CommentsConjunction.findById", query = "SELECT c FROM CommentsConjunction c WHERE c.id = :id")
    , @NamedQuery(name = "CommentsConjunction.findByDateCreated", query = "SELECT c FROM CommentsConjunction c WHERE c.dateCreated = :dateCreated")})
public class CommentsConjunction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentTableId")
    private Collection<Comments> commentsCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "commentsId")
    private Dish dish;

    /* CONSTRUCTORS */
    public CommentsConjunction() {
    }

    public CommentsConjunction(Integer id) {
        this.id = id;
    }

    public CommentsConjunction(Integer id, Date dateCreated) {
        this.id = id;
        this.dateCreated = dateCreated;
    }

    /**
     * Get the id for the comments conjunction
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the comments conjunction id
     * @param id to set to
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Set the date created of the comments
     * @param dateCreated
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return commentsCollection;
    }

    /**
     * Set the comments collection
     * @param commentsCollection to set to
     */
    public void setCommentsCollection(Collection<Comments> commentsCollection) {
        this.commentsCollection = commentsCollection;
    }

    /**
     * Get the dish associated the comments conjunction
     * @return 
     */
    public Dish getDish() {
        return dish;
    }

    /**
     * Set the dish associated with this conjunction table
     * @param dish 
     */
    public void setDish(Dish dish) {
        this.dish = dish;
    }

    /* AUTO-GENERATED FUNCTIONS */
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommentsConjunction)) {
            return false;
        }
        CommentsConjunction other = (CommentsConjunction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.CommentsConjunction[ id=" + id + " ]";
    }
    
}
