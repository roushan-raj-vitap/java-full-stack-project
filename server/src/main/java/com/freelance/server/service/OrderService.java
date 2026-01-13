package com.freelance.server.service;

import com.freelance.server.dto.OrderResponseDto;
import com.freelance.server.model.Gig;
import com.freelance.server.model.Order;
import com.freelance.server.model.OrderStatus;
import com.freelance.server.repository.GigRepository;
import com.freelance.server.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GigRepository gigRepository;

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public List<OrderResponseDto> getOrdersByBuyer(String buyer) {
        List<Order> orders = orderRepository.findByBuyerId(buyer);
        System.out.println(orders);
        return mapOrdersToDto(orders);
    }

    public List<OrderResponseDto> getOrdersBySeller(String seller) {
        List<Order> orders = orderRepository.findBySellerId(seller);
        return mapOrdersToDto(orders);
    }

    private List<OrderResponseDto> mapOrdersToDto(List<Order> orders) {
        List<Long> gigIds = orders.stream().map(Order::getGigId).distinct().toList();
        Map<Long, Gig> gigMap = gigRepository.findAllById(gigIds)
                .stream().collect(Collectors.toMap(Gig::getId, gig -> gig));

        return orders.stream().map(order -> {
            Gig gig = gigMap.get(order.getGigId());
            OrderResponseDto dto = new OrderResponseDto();
            dto.setId(order.getId());
            dto.setBuyerId(order.getBuyerId());
            dto.setSellerId(order.getSellerId());
            dto.setGigId(order.getGigId());
            dto.setStatus(order.getStatus());

            if (gig != null) {
                dto.setGigTitle(gig.getTitle());
                dto.setGigImage(gig.getImageUrl());
                dto.setGigPrice(gig.getPrice());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
