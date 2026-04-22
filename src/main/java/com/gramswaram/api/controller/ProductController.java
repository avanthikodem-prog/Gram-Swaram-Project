package com.gramswaram.api.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gramswaram.api.AIService;
import com.gramswaram.api.model.Product;
import com.gramswaram.api.model.Vendor;
import com.gramswaram.api.repository.ProductRepository;
import com.gramswaram.api.repository.VendorRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*") 
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private AIService aiService;

    @Autowired
    private VendorRepository vendorRepository;

    // డేటాబేస్ నుండి అన్ని ప్రొడక్ట్స్ తీసుకురావడానికి
    @GetMapping("/api/products")
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @PostMapping("/api/products")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product.getVendor() == null || product.getVendor().getId() == null) {
            return ResponseEntity.badRequest().body("Vendor id is required");
        }

        Optional<Vendor> vendor = vendorRepository.findById(product.getVendor().getId());
        if (vendor.isEmpty()) {
            return ResponseEntity.badRequest().body("Vendor not found");
        }

        if (product.getAvailableQuantity() == null) {
            product.setAvailableQuantity(0);
        }
        product.setVendor(vendor.get());
        return ResponseEntity.ok(repository.save(product));
    }

    // పైథాన్ AI నుండి వాయిస్ రెస్పాన్స్ తీసుకుని JSON రూపంలో పంపడానికి
    @GetMapping("/listen-ai")
    public ResponseEntity<Map<String, String>> listenAI() {
        Map<String, String> response = new HashMap<>();
        try {
            // పైథాన్ సర్వర్ నుండి వచ్చే వాయిస్ టెక్స్ట్
            String aiText = aiService.startListening(); 
            
            response.put("status", "success");
            response.put("text", aiText);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "AI Server తో అనుసంధానం కుదరలేదు");
            return ResponseEntity.status(500).body(response);
        }
    }
}