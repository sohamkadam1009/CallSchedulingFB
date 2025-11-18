package com.example.demo.dto;

import lombok.Data;

@Data
public class InvestmentRequest {
    private Long userId;
    private String investmentRange;
}
