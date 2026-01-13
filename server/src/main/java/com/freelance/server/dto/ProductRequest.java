package com.freelance.server.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Long amount; // In cents
    private Long quantity;
    private String currency;
    private String connectedAccountId;
    private Long platformFeeAmount;
    
    private String buyerId;
    private String sellerId;
    private Long gigId;
}
