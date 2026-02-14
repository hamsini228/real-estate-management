package com.cdac.realestate.service;

import com.cdac.realestate.entity.Property;
import com.cdac.realestate.entity.User;
import com.cdac.realestate.repository.PropertyRepository;
import com.cdac.realestate.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    UserRepository userRepository;

<<<<<<< HEAD
    public Property addProperty(Property property, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
=======
    public Property addProperty(Property property, org.springframework.web.multipart.MultipartFile image, Long sellerId) throws java.io.IOException {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            property.setImages(imageUrl);
        }

>>>>>>> caa1517116c0cfdc5e3c3b03f54b8f09f8d6c083
        property.setSeller(seller);
        property.setStatus(Property.PropertyStatus.PENDING); // Default pending
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties(String city, Double minPrice, Double maxPrice,
            Property.PropertyType type, Integer beds) {

        Specification<Property> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

<<<<<<< HEAD
            // Only APPROVED properties for public search
            predicates.add(cb.equal(root.get("status"), Property.PropertyStatus.APPROVED));
=======
            // APPROVED and SOLD properties for public search
            predicates.add(cb.or(
                    cb.equal(root.get("status"), Property.PropertyStatus.APPROVED),
                    cb.equal(root.get("status"), Property.PropertyStatus.SOLD)));
>>>>>>> caa1517116c0cfdc5e3c3b03f54b8f09f8d6c083

            if (StringUtils.hasText(city)) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (beds != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("beds"), beds));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return propertyRepository.findAll(spec);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public List<Property> getPropertiesBySeller(Long sellerId) {
        return propertyRepository.findBySellerId(sellerId);
    }

    public List<Property> getPendingProperties() {
        return propertyRepository.findByStatus(Property.PropertyStatus.PENDING);
    }

    public Property updateStatus(Long id, Property.PropertyStatus status, String reason) {
        Property property = getPropertyById(id);
        property.setStatus(status);
        if (status == Property.PropertyStatus.REJECTED && reason != null) {
            property.setRejectionReason(reason);
        } else if (status == Property.PropertyStatus.APPROVED) {
            property.setRejectionReason(null); // Clear reason if approved
        }
        return propertyRepository.save(property);
    }

<<<<<<< HEAD
    public Property updateProperty(Long id, Property propertyDetails, Long sellerId) {
=======
    public Property updateProperty(Long id, Property propertyDetails, org.springframework.web.multipart.MultipartFile image, Long sellerId) throws java.io.IOException {
>>>>>>> caa1517116c0cfdc5e3c3b03f54b8f09f8d6c083
        Property property = getPropertyById(id);

        if (!property.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this property");
        }

        property.setTitle(propertyDetails.getTitle());
        property.setDescription(propertyDetails.getDescription());
        property.setPrice(propertyDetails.getPrice());
        property.setAddress(propertyDetails.getAddress());
        property.setCity(propertyDetails.getCity());
        property.setType(propertyDetails.getType());
        property.setArea(propertyDetails.getArea());
        property.setBeds(propertyDetails.getBeds());
        property.setBaths(propertyDetails.getBaths());
        property.setBhk(propertyDetails.getBhk());
<<<<<<< HEAD
        property.setImages(propertyDetails.getImages());

        // Reset status to PENDING on edit? Or keep as is? Usually re-approval needed if
        // critical fields change.
        // For now, let's keep status but maybe Admin wants to re-verify.
        // Let's set to PENDING to be safe.
=======
        
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
             property.setImages(imageUrl);
        } else if (propertyDetails.getImages() != null && !propertyDetails.getImages().isEmpty()) {
             property.setImages(propertyDetails.getImages());
        }
>>>>>>> caa1517116c0cfdc5e3c3b03f54b8f09f8d6c083
        property.setStatus(Property.PropertyStatus.PENDING);

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
<<<<<<< HEAD
=======
    
    private String saveImage(org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        String uploadDir = "uploads";
        java.io.File directory = new java.io.File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Generate unique filename
        String fileName = java.util.UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir, fileName);
        
        // Save file locally
        java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        
        // Return accessible URL instead of file path
        // This solves the browser security issue by serving via HTTP
        return "http://localhost:8081/uploads/" + fileName;
    }
>>>>>>> caa1517116c0cfdc5e3c3b03f54b8f09f8d6c083
}
