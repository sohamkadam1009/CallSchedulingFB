package com.example.demo.Sevices;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Models.Booking;
import com.example.demo.Models.UserTemp;
import com.example.demo.Repositories.BookingRepository;
import com.example.demo.Repositories.UserTempRepository;
import com.example.demo.dto.CheckSlotResponse;
import com.example.demo.dto.CreateBookingRequest;
import com.example.demo.dto.InvestmentRequest;
import com.example.demo.dto.SendOtpRequest;
import com.example.demo.dto.StartRequest;
import com.example.demo.dto.VerifyOtpRequest;
import com.example.demo.util.OtpUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFlowService {

    private final UserTempRepository userTempRepository;
    private final BookingRepository bookingRepository;

    // ----------------------------------------------------
    // STEP 1: Start → Save Name
    // ----------------------------------------------------
    public UserTemp start(StartRequest req) {
        UserTemp u = UserTemp.builder()
                .fullName(req.getFullName())
                .otpVerified(false)
                .build();

        return userTempRepository.save(u);
    }

    // ----------------------------------------------------
    // STEP 2: Send OTP
    // ----------------------------------------------------
    @Transactional
    public UserTemp sendOtp(SendOtpRequest req) {
        UserTemp u = userTempRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        u.setMobile(req.getMobile());

        String otp = OtpUtil.generate4DigitOtp();   // Example: 1234
        u.setOtp(otp);
        u.setOtpVerified(false);

        return userTempRepository.save(u);
    }

    // ----------------------------------------------------
    // STEP 2b: Verify OTP
    // ----------------------------------------------------
    @Transactional
    public boolean verifyOtp(VerifyOtpRequest req) {
        UserTemp u = userTempRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (u.getOtp() != null && u.getOtp().equals(req.getOtp())) {
            u.setOtpVerified(true);
            userTempRepository.save(u);
            return true;
        }
        return false;
    }

    // ----------------------------------------------------
    // STEP 3: Investment Selection
    // ----------------------------------------------------
    @Transactional
    public boolean setInvestment(InvestmentRequest req) {
        UserTemp u = userTempRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        u.setInvestmentRange(req.getInvestmentRange());
        userTempRepository.save(u);

        // Below 50L is not eligible
        return !"BELOW_50L".equalsIgnoreCase(req.getInvestmentRange());
    }

    // ----------------------------------------------------
    // STEP 4: Check Slot Availability
    // ----------------------------------------------------
    public CheckSlotResponse checkSlot(LocalDate date, LocalTime time) {
        Long count = bookingRepository.countByDateAndTime(date, time);
        long bookingCount = Optional.ofNullable(count).orElse(0L);

        return new CheckSlotResponse(bookingCount < 5, bookingCount);
    }

    // ----------------------------------------------------
    // STEP 5: Create Final Booking
    // ----------------------------------------------------
    @Transactional
    public Booking createBooking(CreateBookingRequest req) {

        // Validate user
        UserTemp u = userTempRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!u.isOtpVerified()) {
            throw new IllegalStateException("OTP not verified");
        }

        if ("BELOW_50L".equalsIgnoreCase(u.getInvestmentRange())) {
            throw new IllegalStateException("User not eligible");
        }

        LocalDate date = req.getDate();
        LocalTime time = req.getTime();

        // Check slot availability
        Long count = bookingRepository.countByDateAndTime(date, time);
        long bookingCount = Optional.ofNullable(count).orElse(0L);

        if (bookingCount >= 5) {
            throw new IllegalStateException("Slot full");
        }

        // Save booking
        Booking b = Booking.builder()
                .fullName(u.getFullName())
                .mobile(u.getMobile())
                .email(req.getEmail())
                .guestEmail(req.getGuestEmail())
                .message(req.getMessage())
                .investmentRange(u.getInvestmentRange())
                .date(date)
                .time(time)
                .build();

        return bookingRepository.save(b);
    }

    // ----------------------------------------------------
    // ADMIN: GET ALL BOOKINGS
    // ----------------------------------------------------
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
