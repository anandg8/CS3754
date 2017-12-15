/*
 * Created by Ananthavel Guruswamy on 2017.12.13  * 
 * Copyright © 2017 Ananthavel Guruswamy. All rights reserved. * 
 */
package com.mycompany.managers;

import java.util.Date;
import javax.ejb.Local;
import com.mycompany.EntityBeans.Message;
 
/**
 * 
 * @author Anand
 * The Interface for the MessageManager.
 */
@Local
public interface MessageManagerLocal {
 
    void sendMessage(Message msg);
 
    Message getFirstAfter(Date after);
 
}
