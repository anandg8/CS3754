/*
 * Created by Ananthavel Guruswamy on 2017.12.13  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.EntityBeans;
import com.mycompany.managers.MessageManagerLocal;
import org.primefaces.context.RequestContext;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
 
/**
 *
 * @author Anand
 */
@ManagedBean
@ViewScoped
public class MessageBean implements Serializable {
 
    @EJB
    MessageManagerLocal mm; // the bean for the message manager.
 
    private final List messages; // the list of messages.
    private Date lastUpdate; // the date of the last message that was sent.
    private Message message; // the Message object.
 
    /**
     * Creates a new instance of MessageBean.
     */
    public MessageBean() {
        messages = Collections.synchronizedList(new LinkedList());
        lastUpdate = new Date(0);
        message = new Message();
    }
 
    public Date getLastUpdate() {
        return lastUpdate;
    }
 
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
 
    public Message getMessage() {
        return message;
    }
 
    public void setMessage(Message message) {
        this.message = message;
    }
 
    public void sendMessage(ActionEvent evt) {
        mm.sendMessage(message);
    }
 
    public void firstUnreadMessage(ActionEvent evt) {
       // Get the correct chat component to update it programatically.
       RequestContext ctx = RequestContext.getCurrentInstance();
       
       Message m = mm.getFirstAfter(lastUpdate); // get the latest message that was send.
 
       ctx.addCallbackParam("ok", m!=null); // declare the callback function.
       if(m==null)
           return;
 
       lastUpdate = m.getDateSent(); // update the message box with the message that was last sent.
 
       // invoke the callback function in the JavaScript.
       ctx.addCallbackParam("user", m.getUser());
       ctx.addCallbackParam("dateSent", m.getDateSent().toString());
       ctx.addCallbackParam("text", m.getMessage());
 
    }
 
}
