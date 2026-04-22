package com.gramswaram.api.dto;

public class OtpVerifyRequest {
    private String phone;
    private String role;
    private String otp;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
