package com.freelance.server.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.freelance.server.dto.GigDto;
import com.freelance.server.dto.GigResponseDto;
import com.freelance.server.model.Gig;
import com.freelance.server.service.GigService;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/gigs")
public class GigController {

	@Autowired
	GigService gigService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createGig(@ModelAttribute GigDto gigDto, Principal principal) {
        return gigService.createGig(gigDto, principal.getName());
    }
	
	@GetMapping
	@PreAuthorize("hasAnyRole('FREELANCER','CLIENT')")
    public List<GigResponseDto> getAllGigs() {
        return gigService.getAllGigs();
    }
	@GetMapping("/{Id}")
	public GigResponseDto getGigById(@PathVariable("Id") Long id){
		return gigService.getGigById(id);
	}

    @GetMapping("/me")
    @PreAuthorize("hasRole('FREELANCER')")
    public List<Gig> getMyGigs(Principal principal) {
        return gigService.getMyGigs(principal.getName());
    }

    @PutMapping(path="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<?> updateGig(@PathVariable Long id, @ModelAttribute GigDto dto, Principal principal) {
        return gigService.updateGig(id, dto, principal.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<?> deleteGig(@PathVariable Long id, Principal principal) {
        return gigService.deleteGig(id, principal.getName());
    }
}
