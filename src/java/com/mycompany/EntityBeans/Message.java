/*
 * Created by Ananthavel Guruswamy on 2017.12.13  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.util.Date;
 
/**
 * Define a Message Object. Implement Serializable so that the message content is serialized when it's transmitted.
 * @author Anand
 */
public class Message implements Serializable {
    private Date dateSent; // the time the message was sent.
    private String user; // the username of the user sending the message.
    private String message; // the content of the message.
 
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
