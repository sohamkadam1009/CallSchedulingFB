package com.example.demo.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private Long userId;
    private String otp;
}
