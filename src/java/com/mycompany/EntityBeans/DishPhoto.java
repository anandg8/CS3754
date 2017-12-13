/*
 * Created by Ananthavel Guruswamy on 2017.12.12  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anand
 */
@Entity
@Table(name = "DishPhoto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DishPhoto.findAll", query = "SELECT d FROM DishPhoto d")
    , @NamedQuery(name = "DishPhoto.findById", query = "SELECT d FROM DishPhoto d WHERE d.id = :id")
    , @NamedQuery(name = "DishPhoto.findByExtension", query = "SELECT d FROM DishPhoto d WHERE d.extension = :extension")
    , @NamedQuery(name = "DishPhoto.findByDishId", query = "SELECT d FROM DishPhoto d WHERE d.dishId = :dishId")})
public class DishPhoto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;
    @Column(name = "dish_id")
    private Integer dishId;

    public DishPhoto() {
    }

    public DishPhoto(Integer id) {
        this.id = id;
    }

    public DishPhoto(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
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
        if (!(object instanceof DishPhoto)) {
            return false;
        }
        DishPhoto other = (DishPhoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.DishPhoto[ id=" + id + " ]";
    }
    
}
