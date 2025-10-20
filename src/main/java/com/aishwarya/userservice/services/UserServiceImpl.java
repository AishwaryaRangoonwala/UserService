package com.aishwarya.userservice.services;

import com.aishwarya.userservice.exceptions.InvalidTokenException;
import com.aishwarya.userservice.exceptions.PasswordMismatchException;
import com.aishwarya.userservice.models.Role;
import com.aishwarya.userservice.models.Token;
import com.aishwarya.userservice.models.User;
import com.aishwarya.userservice.repositories.RoleRepository;
import com.aishwarya.userservice.repositories.TokenRepository;
import com.aishwarya.userservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private RoleRepository roleRepository;
    private SecretKey secretKey;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           RoleRepository roleRepository,
                           SecretKey secretKey) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.secretKey = secretKey;
    }

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return   optionalUser.get();
        }
        // If the email is not present in the DB
        // then create a new user and save it to the database
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
         Optional<Role> optionalRole = roleRepository.findByRoleName("STUDENT");
         optionalRole.ifPresent(role -> user.getRoles().add(role));
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) throws PasswordMismatchException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            // redirect the user to the signup page
            return null;
        }
        User user = optionalUser.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            // Password Mismatch Exception
            throw new PasswordMismatchException("Invalid password.");
        }
        // Login successful
        // Generate the token
//        Token token = new Token();
//        token.setUser(user);
//        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date expiryDate = calendar.getTime();
        // Generate a JWT token using JJWT
        // Let's not harcode the payload, instead create the payload
        // with the user details
        String userData = "{\n" +
                "  \"email\": \"rangoonwalaaishwarya@gmail.com\",\n" +
                "  \"roles\": [\n" +
                "    \"instructor\",\n" +
                "    \"ta\"\n" +
                "  ],\n" +
                "  \"expiryDate\": \"22ndSept2026\"\n" +
                "}";
        // TODO: Try to generate header and signature
        // Claims == Payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("exp", expiryDate);
        // byte[] payload = userData.getBytes(StandardCharsets.UTF_8);
        // String tokenValue = Jwts.builder().content(payload).compact();
        String jwtToken = Jwts.builder().claims(claims).signWith(secretKey).compact();
        return jwtToken;
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException {
//        Optional<Token> tokenOptional =
//                tokenRepository.findByTokenValueAndExpiryDateAfter(tokenValue, new Date());
//        if (tokenOptional.isEmpty()) {
//            // token is invalid or either expired
//            throw new InvalidTokenException("Invalid token, either expired or token has expired.");
//        }

        // Token is valid
        /*
        1. Create a parse
        2. Get the claims and verify the token
        3.
         */
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(tokenValue).getPayload();
        // Date expiryDate = (Date) claims.get("exp");
        // Date currentDate = new Date();
//        if (expiryDate.after(currentDate)) {
//            Long userId = (Long) claims.get("userId");
//            Optional<User> optionalUser = userRepository.findById(userId);
//            return optionalUser.get();
//        }
        // Mutiplied by 1000 to get in milliseconds
        Long expiryTime = (Long) claims.get("exp") * 1000L;
        Long currentTime = System.currentTimeMillis();

        if (expiryTime > currentTime) {
            Integer userId =  (Integer) claims.get("userId");
            Optional<User> optionalUser = userRepository.findById(Long.valueOf(userId));
            return optionalUser.get();
        }
        // Token is valid

        return null;
    }
}
