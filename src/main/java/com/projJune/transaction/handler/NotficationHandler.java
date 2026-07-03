package com.projJune.transaction.handler;

import com.projJune.transaction.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotficationHandler {

    @Transactional(propagation = Propagation.NEVER)
    public void sendOrderConformationNotification(Order order){
        //send an email notification to the customer
        System.out.println(order.getId()+ "Order placed sucessfullt");
    }
}
