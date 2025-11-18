package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.UserTemp;

public interface UserTempRepository extends JpaRepository<UserTemp, Long> {
    Optional<UserTemp> findByMobile(String mobile);
}
