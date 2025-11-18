package com.example.demo.Repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByDateAndTime(LocalDate date, LocalTime time);
}
