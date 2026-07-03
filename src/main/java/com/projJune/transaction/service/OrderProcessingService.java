package com.projJune.transaction.service;

import com.projJune.transaction.entity.Order;
import com.projJune.transaction.entity.Product;
import com.projJune.transaction.handler.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    private final OrderHandler orderHandler;

    private final InventoryHandler inventoryHandler;

    private final AuditLogHandler  auditLogHandler;

    private final PaymentValidatorHandler paymentValidatorHandler;

    private final NotficationHandler notficationHandler;


    public OrderProcessingService(OrderHandler orderHandler, InventoryHandler inventoryHandler, AuditLogHandler auditLogHandler, PaymentValidatorHandler paymentValidatorHandler, NotficationHandler notficationHandler) {
        this.orderHandler = orderHandler;
        this.inventoryHandler = inventoryHandler;
        this.auditLogHandler = auditLogHandler;
        this.paymentValidatorHandler = paymentValidatorHandler;
        this.notficationHandler = notficationHandler;
    }

    //REQUIRED: join an existing transaction or create a new one if not exist
    //REQUIRED_NEW: Always create a new transaction, suspending if any existing transaction
    //MANDATORY: require an existing transaction, if nothing found it will throw exception
    //NEVER: Ensure the method will run without transacation, throw an exception if found any
    //NOT_SUPPORTED: Execute method without transaction, suspending any active transaction
    //SUPPORTS: Supports if there is any active transaction, if not execute without active transaction


    //isolaton: controls the visibility of changes made by one transaction to other transaction
     @Transactional( propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Order placeOrder(Order order){

        Order saveOrder= null;
        //get product inventory

         Product product = inventoryHandler.getProduct(order.getProductId());

        //validate stock availability <5
        validateStockAvailability(order, product);
        try {
            //update total price in order entity
            order.setTotalPrice(order.getQuantity() * product.getPrice());
            //save order
             saveOrder = orderHandler.saveOrder(order);
            //update stock in inventory
            updateStockInventory(order, product);
            auditLogHandler.logAuditDetails(order, "Order placement succeeded");
        } catch (Exception e) {
            auditLogHandler.logAuditDetails(order, "Order placement failed");
        }
        //retries 3 times
       //  notficationHandler.sendOrderConformationNotification(order);

      //  paymentValidatorHandler.validatePayment(order);
        return saveOrder;
    }

    public void processOrder(Order order){
        Order saveOrder = placeOrder(order);
        notficationHandler.sendOrderConformationNotification(order);
    }

    private static void validateStockAvailability(Order order, Product product) {
        if(order.getQuantity()> product.getStockQuantiy()){
            throw new RuntimeException("Insufficient Stock!");
        }
    }


    private void updateStockInventory(Order order, Product product) {
        int availableStock = product.getStockQuantiy() - order.getQuantity();
        product.setStockQuantiy(availableStock);
        inventoryHandler.updateProductDetails(product);
    }
}
