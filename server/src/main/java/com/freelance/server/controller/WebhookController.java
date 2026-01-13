package com.freelance.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelance.server.model.Order;
import com.freelance.server.model.OrderStatus;
import com.freelance.server.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class WebhookController {

 @Value("${stripe.webhookSecret}")
 private String webhookSecret;

 private final OrderRepository orderRepository;
 private final ObjectMapper objectMapper;

 public WebhookController(OrderRepository orderRepository, ObjectMapper objectMapper) {
     this.orderRepository = orderRepository;
     this.objectMapper = objectMapper;
 }
 
 @PostMapping("/webhook")
 public ResponseEntity<String> handleStripeWebhook(
         @RequestHeader("Stripe-Signature") String sigHeader,
         @RequestBody String payload) throws StripeException {
     Event event;
     System.out.print("webhook controller is getting call");
     try {
         event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
     } catch (SignatureVerificationException e) {
         System.out.println("❌ Webhook signature verification failed: " + e.getMessage());
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
     }

     System.out.println("✅ Webhook verified: " + event.getType());

     if ("checkout.session.completed".equals(event.getType())) {
    	    Session session = (Session) event.getDataObjectDeserializer()
    	        .getObject()
    	        .orElse(null);
    	    System.out.println("I am in if condition in completed");
    	    if (session == null) {
    	        return ResponseEntity.badRequest().build();
    	    }

    	    // ✅ Retrieve PaymentIntent using ID from session
    	    String paymentIntentId = session.getPaymentIntent();
    	    PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

    	    String buyerId = paymentIntent.getMetadata().get("buyerId");
    	    String sellerId = paymentIntent.getMetadata().get("sellerId");
    	    String gigId = paymentIntent.getMetadata().get("gigId");

    	    System.out.println("buyerId: " + buyerId);
    	    System.out.println("sellerId: " + sellerId);
    	    System.out.println("gigId: " + gigId);

    	    // ✅ Proceed to save order if these are not null
    	    if (buyerId != null && sellerId != null && gigId != null) {
    	        Order order = new Order();
    	        order.setBuyerId(buyerId);
    	        order.setSellerId(sellerId);
    	        order.setGigId(Long.valueOf(gigId));
    	        order.setStatus(OrderStatus.PENDING); // Or paid/delivered/etc.
    	        orderRepository.save(order);
    	    }
    	}
     return ResponseEntity.ok("Webhook processed");
 }
}