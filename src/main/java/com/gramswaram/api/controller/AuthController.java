package com.gramswaram.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramswaram.api.dto.OtpRequest;
import com.gramswaram.api.dto.OtpVerifyRequest;
import com.gramswaram.api.model.UserAccount;
import com.gramswaram.api.repository.UserAccountRepository;
import com.gramswaram.api.service.OtpService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
    private final OtpService otpService;
    private final UserAccountRepository userAccountRepository;

    public AuthController(OtpService otpService, UserAccountRepository userAccountRepository) {
        this.otpService = otpService;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody OtpRequest request) {
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            return ResponseEntity.badRequest().body("phone is required");
        }
        if (request.getRole() == null || request.getRole().isBlank()) {
            return ResponseEntity.badRequest().body("role is required (USER or VENDOR)");
        }
        String otp = otpService.generateAndStoreOtp(request.getPhone(), request.getRole());
        return ResponseEntity.ok(Map.of(
            "status", "OTP_SENT",
            "phone", request.getPhone(),
            "role", request.getRole().toUpperCase(),
            "otp", otp
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        if (!otpService.verifyOtp(request.getPhone(), request.getRole(), request.getOtp())) {
            return ResponseEntity.status(401).body(Map.of("status", "INVALID_OTP"));
        }

        String normalizedRole = request.getRole().toUpperCase();
        UserAccount account = userAccountRepository.findByPhoneAndRole(request.getPhone(), normalizedRole)
            .orElseGet(UserAccount::new);
        account.setPhone(request.getPhone());
        account.setRole(normalizedRole);
        account.setVerified(true);
        userAccountRepository.save(account);

        return ResponseEntity.ok(Map.of(
            "status", "LOGIN_SUCCESS",
            "phone", request.getPhone(),
            "role", normalizedRole
        ));
    }
}
