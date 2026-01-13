package com.freelance.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.freelance.server.dto.ApiResponse;
import com.freelance.server.dto.GigDto;
import com.freelance.server.dto.GigResponseDto;
import com.freelance.server.model.Gig;
import com.freelance.server.model.User;
import com.freelance.server.repository.GigRepository;
import com.freelance.server.repository.UserRepository;
import com.freelance.server.exception.*;
@Service
public class GigService {
	
	@Autowired
	GigRepository gigRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AwsService awsService;

	public ResponseEntity<?> createGig(GigDto dto, String freelancerEmail) {
	    if (dto.getTitle() == null || dto.getPrice() <= 0) {
	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse("Invalid gig data provided.", false));
	    }

	    String imageURL = "";
	    try {
	        imageURL = awsService.uploadFile(dto.getImage());
	    } catch (Exception e) {
	        System.out.println(e);
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse("Failed to upload image.", false));
	    }

	    Gig gig = Gig.builder()
	            .title(dto.getTitle())
	            .description(dto.getDescription())
	            .price(dto.getPrice())
	            .category(dto.getCategory())
	            .deliveryDays(dto.getDeliveryTime())
	            .imageUrl(imageURL)
	            .freelancerEmail(freelancerEmail)
	            .build();

	    Gig savedGig = gigRepository.save(gig);

	    // ✅ Convert to response DTO
	    GigResponseDto response = GigResponseDto.builder()
	            .id(savedGig.getId())
	            .title(savedGig.getTitle())
	            .description(savedGig.getDescription())
	            .price(savedGig.getPrice())
	            .category(savedGig.getCategory())
	            .deliveryTime(savedGig.getDeliveryDays())
	            .imageUrl(savedGig.getImageUrl())
	            .email(savedGig.getFreelancerEmail()) // if you have this field
	            .build();

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(response);  // ✅ return the actual created gig
	}


	public List<GigResponseDto> getAllGigs() {
	    List<Gig> gigs = gigRepository.findAll();
	    List<GigResponseDto> response = new ArrayList<>();

	    for (Gig g : gigs) {
	        GigResponseDto dto = GigResponseDto.builder()
	                .id(g.getId())
	                .title(g.getTitle())
	                .description(g.getDescription())
	                .price(g.getPrice())
	                .category(g.getCategory())
	                .deliveryTime(g.getDeliveryDays())
	                .imageUrl(g.getImageUrl())
	                .build();

	        response.add(dto);
	    }

	    return response;
	}


	public List<Gig> getMyGigs(String freelancerEmail) {
        return gigRepository.findByFreelancerEmail(freelancerEmail);
    }
	
	public ResponseEntity<?> deleteGig(Long gigId, String freelancerEmail) {
	    Optional<Gig> optionalGig = gigRepository.findById(gigId);

	    if (optionalGig.isEmpty()) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse("Gig not found with ID: " + gigId, false));
	    }

	    Gig gig = optionalGig.get();

	    if (!gig.getFreelancerEmail().equals(freelancerEmail)) {
	        return ResponseEntity
	                .status(HttpStatus.FORBIDDEN)
	                .body(new ApiResponse("You are not authorized to delete this gig.", false));
	    }

	    gigRepository.deleteById(gigId);

	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(new ApiResponse("Gig deleted successfully.", true));
	}


	public ResponseEntity<?> updateGig(Long id, GigDto dto, String freelancerEmail) {
	    Gig gig = gigRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Gig not found"));

	    if (!gig.getFreelancerEmail().equals(freelancerEmail)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	            .body(new ApiResponse("You can update only your own gigs", false));
	    }

	    // Check if new image is uploaded
	    if (dto.getImage() != null && !dto.getImage().isEmpty()) {
	        awsService.deleteFileFromS3(gig.getImageUrl()); // delete old

	        try {
	            String newImageUrl = awsService.uploadFile(dto.getImage()); // upload new
	            gig.setImageUrl(newImageUrl);
	        } catch (IOException e) {
	            // Log and return error response or continue gracefully
	            e.printStackTrace(); // for debugging, use logger in production
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(new ApiResponse("Failed to upload image", false));
	        }
	    }

	    // Update other fields
	    gig.setTitle(dto.getTitle());
	    gig.setDescription(dto.getDescription());
	    gig.setPrice(dto.getPrice());
	    gig.setCategory(dto.getCategory());
	    gig.setDeliveryDays(dto.getDeliveryTime());

	    gigRepository.save(gig);

	    return ResponseEntity.ok(new ApiResponse("Gig updated successfully", true));
	}


	public GigResponseDto getGigById(Long id) {
		// TODO Auto-generated method stub
		Gig gig = gigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Gig not found with id: " + id));
		User user = userRepository.findByEmail(gig.getFreelancerEmail());
		if (user == null) {
		    throw new ResourceNotFoundException("Freelancer not found with email: " + gig.getFreelancerEmail());
		}
		GigResponseDto response = GigResponseDto.builder()
				.id(gig.getId())
	            .title(gig.getTitle())
	            .description(gig.getDescription())
	            .price(gig.getPrice())
	            .category(gig.getCategory())
	            .deliveryTime(gig.getDeliveryDays())
	            .imageUrl(gig.getImageUrl())
	            .email(gig.getFreelancerEmail())
	            .name(user.getName())
	            .connectedAccountId(user.getStripeAccountId())// if you have this field
	            .build();
				
		return response;
	}


}
