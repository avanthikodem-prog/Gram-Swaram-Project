package com.gramswaram.api.controller; 

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramswaram.api.dto.PlaceOrderRequest;
import com.gramswaram.api.model.Order;
import com.gramswaram.api.repository.OrderRepository;
import com.gramswaram.api.service.OrderService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> saveSimpleOrder(@RequestBody Order order) {
        try {
            if (order.getItemNames() == null || order.getItemNames().isBlank()) {
                return ResponseEntity.badRequest().body("itemNames is required");
            }
            if (order.getAddress() == null || order.getAddress().isBlank()) {
                return ResponseEntity.badRequest().body("address is required");
            }
            if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) {
                return ResponseEntity.badRequest().body("paymentMethod is required");
            }
            if (order.getTotalPrice() == null) {
                order.setTotalPrice(0.0);
            }
            return ResponseEntity.ok(orderRepository.save(order));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("సమస్య వచ్చింది: " + e.getMessage());
        }
    }

    @PostMapping("/place")
    public ResponseEntity<String> saveOrder(@RequestBody PlaceOrderRequest request) {
        try {
            orderService.placeOrder(request);
            return ResponseEntity.ok("ఆర్డర్ సేవ్ అయింది!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("సమస్య వచ్చింది: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}