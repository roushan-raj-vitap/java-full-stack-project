package com.freelance.server.service;

import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.PaymentIntent;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.freelance.server.dto.ProductRequest;
import com.freelance.server.dto.StripeResponse;
import com.freelance.server.model.User;
import com.freelance.server.repository.UserRepository;
import com.stripe.*;
import jakarta.annotation.PostConstruct;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.Optional;
import com.stripe.model.LoginLink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
@Service
public class StripeService {
	
	@Value("${stripe.secretKey}")
	private String secretKey;
	
	@PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
	
	@Autowired
    private UserRepository userRepository;

    public Account createConnectedAccount(String freelancerEmail) throws StripeException {
        Stripe.apiKey = secretKey;

        AccountCreateParams params = AccountCreateParams.builder()
            .setType(AccountCreateParams.Type.EXPRESS)
            .setCountry("US")
            .setEmail(freelancerEmail)
            .build();

        Account account = Account.create(params);

        // ✅ Save accountId to the user
        Optional<User> optionalUser = Optional.of(userRepository.findByEmail(freelancerEmail));  // ✅

        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found")); 
        user.setStripeAccountId(account.getId());
        userRepository.save(user);
        return account;
    }
	public String createAccountLink(String accountId) throws StripeException {
	    AccountLinkCreateParams params = AccountLinkCreateParams.builder()
	        .setAccount(accountId)
	        .setRefreshUrl("http://localhost:5173/reauth") // adjust for your frontend
	        .setReturnUrl("http://localhost:5173/success")
	        .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
	        .build();

	    AccountLink accountLink = AccountLink.create(params);
	    return accountLink.getUrl();
	}
	
	public PaymentIntent createPaymentIntent(long amount, String connectedAccountId) throws StripeException {
	    PaymentIntentCreateParams.TransferData transferData =
	        PaymentIntentCreateParams.TransferData.builder()
	            .setDestination(connectedAccountId)
	            .build();

	    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
	        .setAmount(amount)
	        .setCurrency("INR")
	        .addPaymentMethodType("card")
	        .setApplicationFeeAmount(1L)
	        .setTransferData(transferData)  // ✅ use the variable
	        .build();

	    return PaymentIntent.create(params);
	}
	public StripeResponse createCheckoutSession(ProductRequest productRequest) {
	    // Step 1: Build Product Data
	    SessionCreateParams.LineItem.PriceData.ProductData productData =
	            SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                    .setName(productRequest.getName())
	                    .build();

	    // Step 2: Build Price Data
	    SessionCreateParams.LineItem.PriceData priceData =
	            SessionCreateParams.LineItem.PriceData.builder()
	                    .setCurrency(productRequest.getCurrency())
	                    .setUnitAmount(productRequest.getAmount())  // in paise
	                    .setProductData(productData)
	                    .build();

	    // Step 3: Build Line Item
	    SessionCreateParams.LineItem lineItem =
	            SessionCreateParams.LineItem.builder()
	                    .setQuantity(productRequest.getQuantity())
	                    .setPriceData(priceData)
	                    .build();

	    // Step 4: Build PaymentIntentData for connected account
	    SessionCreateParams.PaymentIntentData.Builder paymentIntentDataBuilder =
	            SessionCreateParams.PaymentIntentData.builder()
	            .putMetadata("buyerId", productRequest.getBuyerId())
	            .putMetadata("sellerId", productRequest.getSellerId())
	            .putMetadata("gigId", String.valueOf(productRequest.getGigId()))
	                    .setTransferData(
	                            SessionCreateParams.PaymentIntentData.TransferData.builder()
	                                    .setDestination(productRequest.getConnectedAccountId()) // ✅ to freelancer
	                                    .build()
	                    );

	    // Optional: Add platform fee (e.g., 10% of productRequest.getAmount())
	    if (productRequest.getPlatformFeeAmount() != null) {
	        paymentIntentDataBuilder.setApplicationFeeAmount(productRequest.getPlatformFeeAmount());
	    }

	    // Step 5: Final Checkout Session Params
	    SessionCreateParams params =
	            SessionCreateParams.builder()
	                    .setMode(SessionCreateParams.Mode.PAYMENT)
	                    .setSuccessUrl("http://localhost:5173/payment-success")
	                    .setCancelUrl("http://localhost:5173/payment-cancel")
	                    .addLineItem(lineItem)
	                    .setPaymentIntentData(paymentIntentDataBuilder.build())
	                    .build();

	    try {
	        Session session = Session.create(params);
	        return StripeResponse.builder()
	                .status("SUCCESS")
	                .message("Checkout session created")
	                .sessionId(session.getId())
	                .sessionUrl(session.getUrl())
	                .build();
	    } catch (StripeException e) {
	        return StripeResponse.builder()
	        .status("FAILED")
	        .message("Stripe checkout failed: " + e.getMessage())
	        .build();
	    }
	}

	
	public String generateLoginLink(String email) throws StripeException {
		User user = userRepository.findByEmail(email);
	    LoginLink link = LoginLink.createOnAccount(
	        user.getStripeAccountId()
	    );
	    return link.getUrl();
	}


}
