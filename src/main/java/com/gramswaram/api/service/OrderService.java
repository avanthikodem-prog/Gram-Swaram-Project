package com.gramswaram.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gramswaram.api.dto.OrderItemRequest;
import com.gramswaram.api.dto.PlaceOrderRequest;
import com.gramswaram.api.model.Order;
import com.gramswaram.api.model.Product;
import com.gramswaram.api.repository.OrderRepository;
import com.gramswaram.api.repository.ProductRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(PlaceOrderRequest request) {
        validateRequest(request);

        List<Long> productIds = request.getItems().stream()
            .map(OrderItemRequest::getProductId)
            .collect(Collectors.toList());

        Map<Long, Product> productsById = productRepository.findAllById(productIds).stream()
            .collect(Collectors.toMap(Product::getId, p -> p, (a, b) -> a, HashMap::new));

        double totalPrice = 0.0;
        StringBuilder itemNames = new StringBuilder();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productsById.get(itemRequest.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + itemRequest.getProductId());
            }

            int quantity = itemRequest.getQuantity();
            int available = product.getAvailableQuantity() == null ? 0 : product.getAvailableQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero for product: " + product.getId());
            }

            if (available < quantity) {
                throw new IllegalArgumentException("Insufficient stock for: " + product.getItem_name_english());
            }

            product.setAvailableQuantity(available - quantity);
            totalPrice += product.getPrice() * quantity;

            if (!itemNames.isEmpty()) {
                itemNames.append(", ");
            }
            itemNames.append(product.getItem_name_telugu()).append(" x ").append(quantity);
        }

        productRepository.saveAll(productsById.values());

        Order order = new Order();
        order.setItemNames(itemNames.toString());
        order.setAddress(request.getAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    private void validateRequest(PlaceOrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Order payload is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one item is required");
        }
        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }
}
