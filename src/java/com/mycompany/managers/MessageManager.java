/*
 * Created by Ananthavel Guruswamy on 2017.12.13  *
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. *
 */
package com.mycompany.managers;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.mycompany.EntityBeans.Message;

/**
 *
 * @author Anand
 */
@Singleton
@Startup
public class MessageManager implements MessageManagerLocal {

    /* List of the messages sent through chat */
    private final List<Message> messages =
            Collections.synchronizedList(new LinkedList());;

    /*Create a message object with the give params
      Then add the message to the list of messages.*/
    @Override
    public void sendMessage(Message msg) {
        messages.add(msg);
        msg.setDateSent(new Date());
    }

    /*Get all the messages after a certain date */
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
