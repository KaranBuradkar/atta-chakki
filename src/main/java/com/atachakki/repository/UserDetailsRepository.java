package com.atachakki.repository;

import com.atachakki.entity.User;
import com.atachakki.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

  Optional<UserDetails> findByUser(User user);

  Optional<Boolean> existsByIdAndUserId(Long userDetailsId, Long userId);

  Optional<UserDetails> findByUserUsername(String email);

  Optional<UserDetails> findByUserId(Long id);
}