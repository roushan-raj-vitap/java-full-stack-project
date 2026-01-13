package com.freelance.server.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String buyerId;
	private String sellerId;
	private Long gigId;
	
	@Enumerated(EnumType.STRING)
    private OrderStatus status; // e.g., PENDING, COMPLETED
	
}
