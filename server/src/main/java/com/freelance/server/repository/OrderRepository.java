package com.freelance.server.repository;

import com.freelance.server.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerId(String buyerId);
    List<Order> findBySellerId(String sellerId);
}
