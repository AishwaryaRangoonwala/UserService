package com.aishwarya.userservice.services;

import com.aishwarya.userservice.exceptions.InvalidTokenException;
import com.aishwarya.userservice.exceptions.PasswordMismatchException;
import com.aishwarya.userservice.models.Role;
import com.aishwarya.userservice.models.Token;
import com.aishwarya.userservice.models.User;
import com.aishwarya.userservice.repositories.RoleRepository;
import com.aishwarya.userservice.repositories.TokenRepository;
import com.aishwarya.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
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
    public Token login(String email, String password) throws PasswordMismatchException {
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
        Token token = new Token();
        token.setUser(user);
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        token.setExpiryDate(calendar.getTime());
        return tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException {
        Optional<Token> tokenOptional =
                tokenRepository.findByTokenValueAndExpiryDateAfter(tokenValue, new Date());
        if (tokenOptional.isEmpty()) {
            // token is invalid or either expired
            throw new InvalidTokenException("Invalid token, either expired or token has expired.");
        }
        // Token is valid
        return tokenOptional.get().getUser();
    }
}
