package com.projJune.transaction.handler;

import com.projJune.transaction.entity.AuditLog;
import com.projJune.transaction.entity.Order;
import com.projJune.transaction.repo.AuditLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentValidatorHandler {

    @Autowired
    private AuditLogHandler auditLogHandler;
    @Autowired
    private AuditLogRepo auditLogRepo;

    @Transactional(propagation = Propagation.MANDATORY)
    public void validatePayment(Order order){

        //Assume payment processing happens here
        boolean paymentSuccessful = false;

        //if payment is unsuccessful, we log the payment failed
        if(!paymentSuccessful){
            AuditLog paymentFailureLog = new AuditLog();
            paymentFailureLog.setOrderId(Long.valueOf(order.getId()));
            paymentFailureLog.setAction("Payment Failed for Order: "+order.getId());
            paymentFailureLog.setTimestamp(LocalDateTime.now());
            //save the payment failure log
            auditLogRepo.save(paymentFailureLog);
        }
    }
}
