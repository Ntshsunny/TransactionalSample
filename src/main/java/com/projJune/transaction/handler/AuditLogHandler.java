package com.projJune.transaction.handler;

import com.projJune.transaction.entity.AuditLog;
import com.projJune.transaction.entity.Order;
import com.projJune.transaction.repo.AuditLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class AuditLogHandler {

    @Autowired
    private AuditLogRepo auditLogRepo;

    //log audit details(runs in an independent transaaction)
 //   @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditDetails(Order order, String action){
        AuditLog auditLog = new AuditLog();
        auditLog.setOrderId(Long.valueOf(order.getId()));
        auditLog.setAction(action);
        auditLog.setTimestamp(LocalDateTime.now());
        //save the audit log
        auditLogRepo.save(auditLog);
    }
}
