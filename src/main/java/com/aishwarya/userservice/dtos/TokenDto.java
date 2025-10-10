package com.aishwarya.userservice.dtos;

import com.aishwarya.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TokenDto {
    private String tokenValue;
}
