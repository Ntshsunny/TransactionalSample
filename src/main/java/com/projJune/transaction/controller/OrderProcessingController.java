package com.projJune.transaction.controller;

import com.projJune.transaction.entity.Order;
import com.projJune.transaction.service.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OrderProcessingController {

    @Autowired
    private OrderProcessingService orderProcessingService;

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Order order){
        return ResponseEntity.ok(orderProcessingService.placeOrder(order));
    }
}
