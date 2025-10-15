package com.aishwarya.userservice.controllers;

import com.aishwarya.userservice.dtos.LoginRequestDto;
import com.aishwarya.userservice.dtos.SignUpRequestDto;
import com.aishwarya.userservice.dtos.TokenDto;
import com.aishwarya.userservice.dtos.UserDto;
import com.aishwarya.userservice.exceptions.PasswordMismatchException;
import com.aishwarya.userservice.models.Token;
import com.aishwarya.userservice.models.User;
import com.aishwarya.userservice.services.UserService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signup(requestDto.getName(),  requestDto.getEmail(), requestDto.getPassword());
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public TokenDto  login(@RequestBody LoginRequestDto requestDto) throws PasswordMismatchException {
        Token token = userService.login(requestDto.getEmail(), requestDto.getPassword());
        return TokenDto.from(token);
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable("tokenValriable") String tokenValue) {
        return null;
    }
}
