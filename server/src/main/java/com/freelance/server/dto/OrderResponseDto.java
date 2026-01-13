package com.freelance.server.dto;

import com.freelance.server.model.OrderStatus;

import lombok.Data;
@Data
public class OrderResponseDto {
    private Long id;
    private String buyerId;
    private String sellerId;
    private Long gigId;
    private OrderStatus status;

    // Embedded gig details
    private String gigTitle;
    private String gigImage;
    private Double gigPrice;
}
