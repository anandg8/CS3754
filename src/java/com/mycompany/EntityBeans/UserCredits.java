/*
 * Created by Dong Gyu Lee on 2017.12.13  * 
 * Copyright © 2017 Dong Gyu Lee. All rights reserved. * 
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DongGyu
 */
@Entity
@Table(name = "user_credits")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserCredits.findAll", query = "SELECT u FROM UserCredits u")
    , @NamedQuery(name = "UserCredits.findById", query = "SELECT u FROM UserCredits u WHERE u.id = :id")
    , @NamedQuery(name = "UserCredits.findByUserId", query = "SELECT u FROM UserCredits u WHERE u.userId = :userId")
    , @NamedQuery(name = "UserCredits.findByCreditsAvailable", query = "SELECT u FROM UserCredits u WHERE u.creditsAvailable = :creditsAvailable")})
public class UserCredits implements Serializable {

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
    @Column(name = "credits_available")
    private int creditsAvailable;

    public UserCredits() {
    }

    public UserCredits(Integer id) {
        this.id = id;
    }

    public UserCredits(Integer id, int userId, int creditsAvailable) {
        this.id = id;
        this.userId = userId;
        this.creditsAvailable = creditsAvailable;
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

    public int getCreditsAvailable() {
        return creditsAvailable;
    }

    public void setCreditsAvailable(int creditsAvailable) {
        this.creditsAvailable = creditsAvailable;
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
        if (!(object instanceof UserCredits)) {
            return false;
        }
        UserCredits other = (UserCredits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.UserCredits[ id=" + id + " ]";
    }
    
}
