package com.example.demo.Sevices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DummySmsService implements SmsService {

    private final Logger logger = LoggerFactory.getLogger(DummySmsService.class);

    @Override
    public void sendSms(String mobile, String message) {
        // SMS sending disabled — test mode
        logger.info("[TEST MODE] SMS disabled. OTP: {}", message);
    }
}