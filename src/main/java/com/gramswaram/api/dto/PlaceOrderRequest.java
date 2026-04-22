package com.gramswaram.api.dto;

import java.util.List;

public class PlaceOrderRequest {
    private List<OrderItemRequest> items;
    private String address;
    private String paymentMethod;

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
