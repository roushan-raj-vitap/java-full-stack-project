package com.freelance.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GigResponseDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String category;
    private int deliveryTime;
    private String imageUrl;  // pre-uploaded image URL
    private String name;
    private String email;
    private String connectedAccountId;
}
