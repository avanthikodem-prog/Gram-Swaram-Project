package com.gramswaram.api.model; // ప్యాకేజీ పేరు మార్చాను

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemNames;      
    private String address;        
    private String paymentMethod;  
    private Double totalPrice;     // ఇక్కడ ఇది యాడ్ చెయ్, కంట్రోలర్‌లో వాడుతున్నాం కాబట్టి
    private LocalDateTime orderTime;

    @PrePersist
    public void setOrderTime() {
        this.orderTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemNames() { return itemNames; }
    public void setItemNames(String itemNames) { this.itemNames = itemNames; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
}