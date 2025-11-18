package com.example.demo.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.UserTemp;
import com.example.demo.Sevices.UserFlowService;
import com.example.demo.dto.CreateBookingRequest;
import com.example.demo.dto.InvestmentRequest;
import com.example.demo.dto.SendOtpRequest;
import com.example.demo.dto.StartRequest;
import com.example.demo.dto.VerifyOtpRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserFlowController {

    private final UserFlowService userFlowService;

    // -------------------------
    // STEP 1: Save Full Name
    // -------------------------
    @PostMapping("/start")
    public ResponseEntity<UserTemp> start(@RequestBody StartRequest req) {
        return ResponseEntity.ok(userFlowService.start(req));
    }

    // -------------------------
    // STEP 2: Send OTP
    // -------------------------
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest req) {
        UserTemp u = userFlowService.sendOtp(req);

        // TEST MODE: Return OTP directly
        return ResponseEntity.ok(
                Map.of(
                        "userId", u.getId(),
                        "mobile", u.getMobile(),
                        "otp", u.getOtp()
                )
        );
    }

    // -------------------------
    // STEP 2b: Verify OTP
    // -------------------------
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        boolean ok = userFlowService.verifyOtp(req);

        if (ok)
            return ResponseEntity.ok(Map.of("verified", true));

        return ResponseEntity.badRequest().body(Map.of(
                "verified", false,
                "message", "Invalid OTP"
        ));
    }

    // -------------------------
    // STEP 3: Investment Range
    // -------------------------
    @PostMapping("/investment")
    public ResponseEntity<?> investment(@RequestBody InvestmentRequest req) {
        boolean eligible = userFlowService.setInvestment(req);
        return ResponseEntity.ok(Map.of("eligible", eligible));
    }

    // -------------------------
    // STEP 4: Check Slot Availability
    // -------------------------
    @GetMapping("/check-slot")
    public ResponseEntity<?> checkSlot(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time
    ) {
        return ResponseEntity.ok(userFlowService.checkSlot(date, time));
    }

    // -------------------------
    // STEP 5: Create Final Booking
    // -------------------------
    @PostMapping("/create-booking")
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingRequest req) {
        return ResponseEntity.ok(userFlowService.createBooking(req));
    }

    // -------------------------
    // LIST ALL BOOKINGS (ADMIN)
    // -------------------------
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(userFlowService.getAllBookings());
    }
}
