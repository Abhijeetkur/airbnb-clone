package com.abhijeet.airbnb_backend.controller;

import com.abhijeet.airbnb_backend.dto.ApiResponse;
import com.abhijeet.airbnb_backend.dto.PropertyRequest;
import com.abhijeet.airbnb_backend.dto.PropertyResponse;
import com.abhijeet.airbnb_backend.entity.user.User;
import com.abhijeet.airbnb_backend.service.PropertyService;
import com.abhijeet.airbnb_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abhijeet.airbnb_backend.service.CloudinaryService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api/v1/host")
public class HostController {
    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Void>> getHostDashboard(@AuthenticationPrincipal User currentUser) {
        // This will only work if the user has ROLE_HOST
        return ResponseEntity.ok(new ApiResponse<>(
                "Welcome to the Host Dashboard, " + currentUser.getFname(),
                true,
                LocalDateTime.now().toString()));
    }

    @PostMapping(value = "/properties/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadPropertyImages(HttpServletRequest request) { // Removed strict
                                                                                           // MultipartHttpServletRequest
                                                                                           // binding for debugging
        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new RuntimeException("Request is not a Multipart request. Content-Type: " + request.getContentType());
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("images");

        if (files == null || files.isEmpty()) {
            // Fallback: try to find any files
            if (multipartRequest.getFileMap().isEmpty()) {
                try {
                    Collection<Part> parts = request.getParts();
                    throw new RuntimeException("No files found in request. Content-Type: " + request.getContentType()
                            + ", Content-Length: " + request.getContentLength()
                            + ". Keys: " + multipartRequest.getParameterMap().keySet()
                            + ". Parts: " + parts.size() + " (" + parts + ")");
                } catch (Exception e) {
                    throw new RuntimeException("Failed to read parts: " + e.getMessage(), e);
                }
            }
            files = new ArrayList<>(multipartRequest.getFileMap().values());
        }

        if (files.size() < 5 || files.size() > 15) {
            throw new RuntimeException("Number of images must be between 5 and 15. Received: " + files.size());
        }
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            imageUrls.add(cloudinaryService.uploadFile(file));
        }
        return ResponseEntity.ok(imageUrls);
    }

    @PostMapping("/properties")
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody PropertyRequest propertyRequest,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        PropertyResponse savedProperty = propertyService.addProperty(propertyRequest, currentUser);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }
}
