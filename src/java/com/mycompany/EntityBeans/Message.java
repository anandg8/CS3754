/*
 * Created by Ananthavel Guruswamy on 2017.12.13  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
 
/**
 * Message class
 * @author Anand
 */
public class Message implements Serializable {
    private Date dateSent;
    private String user;
    private String message;
 
    public Date getDateSent() {
        return dateSent;
    }
 
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getUser() {
        return user;
    }
 
    public void setUser(String user) {
        this.user = user;
    }
}
