package com.gramswaram.api.repository; // ప్యాకేజీ పేరు మార్చాను

import com.gramswaram.api.model.Order; // Order ని ఇంపోర్ట్ చేశాను
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}