package com.gramswaram.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramswaram.api.dto.OneClickJoinRequest;
import com.gramswaram.api.model.Product;
import com.gramswaram.api.model.Vendor;
import com.gramswaram.api.repository.ProductRepository;
import com.gramswaram.api.repository.VendorRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vendors")
public class VendorController {
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;

    public VendorController(VendorRepository vendorRepository, ProductRepository productRepository) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @PostMapping
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @PostMapping("/one-click-join")
    public ResponseEntity<?> oneClickJoin(@RequestBody OneClickJoinRequest request) {
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            return ResponseEntity.badRequest().body("phone is required");
        }

        Vendor vendor = new Vendor();
        vendor.setPhone(request.getPhone());
        vendor.setName(request.getShopName() == null || request.getShopName().isBlank() ? "New Rural Shop" : request.getShopName());
        vendor.setLocation(request.getLocation() == null ? "Location pending" : request.getLocation());
        vendor.setLatitude(request.getLatitude());
        vendor.setLongitude(request.getLongitude());
        vendor.setShopPhotoUrl(request.getShopPhotoUrl());

        Vendor saved = vendorRepository.save(vendor);
        return ResponseEntity.ok(Map.of(
            "status", "JOINED",
            "vendorId", saved.getId(),
            "message", "One-click join successful. OTP verification can be completed via /api/auth endpoints."
        ));
    }

    @GetMapping("/nearby")
    public List<Vendor> getNearbyVendors(@RequestParam("lat") double userLat, @RequestParam("lng") double userLng,
                                         @RequestParam(value = "radiusKm", defaultValue = "15") double radiusKm) {
        return vendorRepository.findAll().stream()
            .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
            .filter(v -> haversineKm(userLat, userLng, v.getLatitude(), v.getLongitude()) <= radiusKm)
            .toList();
    }

    @GetMapping("/{vendorId}/products")
    public ResponseEntity<List<Product>> getProductsByVendor(@PathVariable Long vendorId) {
        return vendorRepository.findById(vendorId)
            .map(vendor -> ResponseEntity.ok(productRepository.findByVendorId(vendorId)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}
