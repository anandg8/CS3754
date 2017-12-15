/*
 * Created by Ananthavel Guruswamy on 2017.12.13  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.managers;
import com.mycompany.EntityBeans.Message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import javax.ejb.Singleton;
import javax.ejb.Startup;
 
/**
 * 
 * @author Anand
 */
@Singleton
@Startup
public class MessageManager implements MessageManagerLocal {
 
    // create a linked-list to hold all the messages
    private final List<Message> messages =
            Collections.synchronizedList(new LinkedList());;
 
    @Override
    /*
    Add a message to the list.
    */
    public void sendMessage(Message msg) {
        messages.add(msg); // add a message to the list.
        msg.setDateSent(new Date()); // update the date of the message.
    }

    /*
    Gets the first message after a certain date to display in the message box.
    */
    @Override
    public Message getFirstAfter(Date after) {
        if(messages.isEmpty())
            return null;
        if(after == null)
            return messages.get(0);
        for(Message m : messages)
        {
            if(m.getDateSent().after(after))
                return m;
        }
        return null;
    }
 
}
