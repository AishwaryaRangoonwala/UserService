package com.aishwarya.userservice.services;

import com.aishwarya.userservice.exceptions.PasswordMismatchException;
import com.aishwarya.userservice.models.Token;
import com.aishwarya.userservice.models.User;

public interface UserService {
    User signup(String name, String email, String password);

    Token login(String email, String password) throws PasswordMismatchException;

    User validateToken(String tokenValue);
}
