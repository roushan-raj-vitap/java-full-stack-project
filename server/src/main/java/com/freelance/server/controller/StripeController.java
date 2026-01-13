package com.freelance.server.controller;

import com.freelance.server.dto.ProductRequest;
import com.freelance.server.dto.StripeResponse;
import com.freelance.server.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;
    
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody ProductRequest request) {
        StripeResponse response = stripeService.createCheckoutSession(request);
        return ResponseEntity.ok(response);
    }

    // 1️⃣ Create Connected Account for a freelancer
    @PostMapping("/create-account")
    public ResponseEntity<?> createConnectedAccount(@RequestParam String email) {
        try {
            Account account = stripeService.createConnectedAccount(email);
            return ResponseEntity.ok(account.getId()); // return the connected account ID
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Failed to create account: " + e.getMessage());
        }
    }

    // 2️⃣ Create Onboarding Link (to complete Stripe Express account setup)
    @GetMapping("/account-link")
    public ResponseEntity<?> generateAccountLink(@RequestParam String accountId) {
        try {
            String url = stripeService.createAccountLink(accountId);
            return ResponseEntity.ok(url); // send back onboarding link
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Failed to create account link: " + e.getMessage());
        }
    }

    // 3️⃣ Create a PaymentIntent for a connected account
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPaymentIntent(
            @RequestParam long amount,
            @RequestParam String accountId) {
        try {
            PaymentIntent intent = stripeService.createPaymentIntent(amount, accountId);
            return ResponseEntity.ok(intent.getClientSecret()); // used on frontend for payment confirmation
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Payment intent creation failed: " + e.getMessage());
        }
    }
    
 // 4️⃣ Generate Express Login Link (so freelancer can check their balance/payouts)
    @GetMapping("/login-link")
    public ResponseEntity<?> generateLoginLink(Authentication user) {
        try {
            String url = stripeService.generateLoginLink(user.getName());
            System.out.println("name inside generating url "+user.getName());
            return ResponseEntity.ok(url); // this is the Stripe Express dashboard link
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Login link creation failed: " + e.getMessage());
        }
    }

}
