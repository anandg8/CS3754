/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amitd
 */
@Entity
@Table(name = "cuisine_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuisineType.findAll", query = "SELECT c FROM CuisineType c")
    , @NamedQuery(name = "CuisineType.findById", query = "SELECT c FROM CuisineType c WHERE c.id = :id")
    , @NamedQuery(name = "CuisineType.findByName", query = "SELECT c FROM CuisineType c WHERE c.name = :name")
    , @NamedQuery(name = "CuisineType.findByDescription", query = "SELECT c FROM CuisineType c WHERE c.description = :description")})
public class CuisineType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuisineId")
    private Collection<Dish> dishCollection;

    /**
     * This class is for selecting Cuisines. 
     * Cuisines are pre-loaded in the database and will not be changed
     * During run-time of this application
     * 
     */
    
    /* CONSTRUCTOR */
    public CuisineType() {
    }

    public CuisineType(Integer id) {
        this.id = id;
    }

    public CuisineType(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Get the id for the cuisine type
     * @return the cuisine id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the cuisine id
     * @param id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the name of Cuisine
     * @return the name of the cuisine
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the cuisine
     * @param name to set to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the description of the cuisine
     * Not used
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the cuisine
     * Not used
     * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Dish> getDishCollection() {
        return dishCollection;
    }

    /**
     * Set the dish collection
     * @param dishCollection
     */
    public void setDishCollection(Collection<Dish> dishCollection) {
        this.dishCollection = dishCollection;
    }

    /* AUTO-GENERATED CODE */
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuisineType)) {
            return false;
        }
        CuisineType other = (CuisineType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.CuisineType[ id=" + id + " ]";
    }
    
}
