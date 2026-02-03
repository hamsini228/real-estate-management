package com.cdac.realestate.controller;

import com.cdac.realestate.entity.Property;
import com.cdac.realestate.security.UserDetailsImpl;
import com.cdac.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    // Public search (only approved)
    @GetMapping
    public List<Property> getAllProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Property.PropertyType type,
            @RequestParam(required = false) Integer beds) {
        return propertyService.getAllProperties(city, minPrice, maxPrice, type, beds);
    }

    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyService.getPropertyById(id);
    }

    // Seller: Add Property
    @PostMapping(consumes = { org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority('SELLER')")
    public Property addProperty(
            @RequestPart("property") String propertyJson,
            @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Property property = mapper.readValue(propertyJson, Property.class);
        
        return propertyService.addProperty(property, image, userDetails.getId());
    }

    // Seller: Update Property
    @PutMapping(value = "/{id}", consumes = { org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority('SELLER')")
    public Property updateProperty(
            @PathVariable Long id, 
            @RequestPart("property") String propertyJson,
            @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
            
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Property property = mapper.readValue(propertyJson, Property.class);
        
        return propertyService.updateProperty(id, property, image, userDetails.getId());
    }

    // Seller: My Listings
    @GetMapping("/my-listings")
    @PreAuthorize("hasAuthority('SELLER')")
    public List<Property> getMyProperties(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return propertyService.getPropertiesBySeller(userDetails.getId());
    }

    // Seller: Delete Listing
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SELLER') or hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }

    // Admin: Get Pending
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Property> getPendingProperties() {
        return propertyService.getPendingProperties();
    }

    // Admin: Approve/Reject
    // Admin: Approve/Reject
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Property updateStatus(@PathVariable Long id,
            @RequestParam Property.PropertyStatus status,
            @RequestParam(required = false) String reason) {
        return propertyService.updateStatus(id, status, reason);
    }
}
