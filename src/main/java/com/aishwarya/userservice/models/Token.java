package com.aishwarya.userservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Token extends BaseModel {
    private String tokenValue;
    private Date expiryDate;
    private User user;
}
