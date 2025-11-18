package com.example.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String mobile;

    private String otp;

    @Builder.Default
    private boolean otpVerified = false;

    private String investmentRange; // BELOW_50L, 50L_2CR, 2CR_5CR, ABOVE_10CR

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
