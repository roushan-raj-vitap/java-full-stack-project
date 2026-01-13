package com.freelance.server.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GigDto {

	private String title;
    private String description;
    private double price;
    private String category;
    private int deliveryTime;
    private MultipartFile image;
}
