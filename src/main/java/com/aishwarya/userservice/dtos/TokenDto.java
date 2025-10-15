package com.aishwarya.userservice.dtos;

import com.aishwarya.userservice.models.Role;
import com.aishwarya.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TokenDto {
    private String tokenValue;

    public static TokenDto from(Token token) {
        if (token == null) return null;
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenValue(token.getTokenValue());
        return tokenDto;
    }
}
