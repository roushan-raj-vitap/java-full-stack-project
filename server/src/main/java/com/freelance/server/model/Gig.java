package com.freelance.server.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gig {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String description;

    private double price;

    private String category;

    private int deliveryDays;

    private String imageUrl;

    // Optional: track which freelancer created the gig
    private String freelancerEmail; // extracted from JWT
    
    
    
}
