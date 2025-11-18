package com.example.demo.dto;

import lombok.Data;

@Data
public class SendOtpRequest {
    private Long userId;
    private String mobile;
}