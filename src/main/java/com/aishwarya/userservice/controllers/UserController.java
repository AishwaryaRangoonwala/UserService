package com.aishwarya.userservice.controllers;

import com.aishwarya.userservice.dtos.LoginRequestDto;
import com.aishwarya.userservice.dtos.SignUpRequestDto;
import com.aishwarya.userservice.dtos.TokenDto;
import com.aishwarya.userservice.dtos.UserDto;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto requestDto) {
        return null;
    }

    @PostMapping("/login")
    public TokenDto  login(@RequestBody LoginRequestDto requestDto) {
        return null;
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable("tokenValriable") String tokenValue) {
        return null;
    }
}
