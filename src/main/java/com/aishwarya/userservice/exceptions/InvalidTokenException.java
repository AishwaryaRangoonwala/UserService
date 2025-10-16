package com.aishwarya.userservice.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String message) {
        super(message);
    }
}

// Checked vs Unchecked exceptions
// Compiler will check at compiletime or throw it
// Compiler does not enforce you to throw or catch it
