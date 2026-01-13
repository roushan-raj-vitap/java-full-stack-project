package com.freelance.server.controller;

import com.freelance.server.dto.OrderResponseDto;
import com.freelance.server.model.Order;
import com.freelance.server.model.Role;
import com.freelance.server.model.User;
import com.freelance.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create order
    @PostMapping
    public Order placeOrder(@RequestBody Order order, Authentication authentication) {
        String buyerId = authentication.getName();  // from JWT
        order.setBuyerId(buyerId);
        return orderService.createOrder(order);
    }

    // Get orders for client
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT','FREELANCER')")
    public ResponseEntity<List<OrderResponseDto>> getOrders(@AuthenticationPrincipal User user) {
        if (user.getRole() == Role.CLIENT) {
            return ResponseEntity.ok(orderService.getOrdersByBuyer(user.getEmail()));
        } else if (user.getRole() == Role.FREELANCER) {
            return ResponseEntity.ok(orderService.getOrdersBySeller(user.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
