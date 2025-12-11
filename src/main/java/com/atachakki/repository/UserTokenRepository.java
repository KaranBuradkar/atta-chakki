package com.atachakki.repository;

import com.atachakki.entity.User;
import com.atachakki.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByUser(User user);

    Optional<UserToken> findByRefreshToken(String refreshToken);
}