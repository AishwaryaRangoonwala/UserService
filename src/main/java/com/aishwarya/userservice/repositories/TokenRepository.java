package com.aishwarya.userservice.repositories;

import com.aishwarya.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    // Declared queries -> Hibernate
    Optional<Token> findByTokenValueAndExpiryDateAfter(String tokenValue, Date currentDate);
}
