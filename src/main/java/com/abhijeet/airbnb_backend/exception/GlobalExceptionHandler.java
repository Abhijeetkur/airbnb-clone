package com.abhijeet.airbnb_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        // Industry Standard: RFC 7807 Problem Detail
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Email Already Registered");
        problemDetail.setType(URI.create("https://docs.abhijeet-airbnb.com/errors/email-exists"));
        problemDetail.setProperty("timestamp", java.time.Instant.now());
        return problemDetail;
    }

    // Fallback for any other unexpected errors (Industry standard: Don't leak details)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexcepted error occurred. Please try again later."
        );
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Your request contains invalid data."
        );
        problemDetail.setTitle("Validat ion Failed");
        problemDetail.setType(URI.create("https://docs.abhijeet-airbnb.com/errors/validation-error"));

        // Extracting field-specific errors
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Invalid value"
//                        (existing, replacement) -> existing // Keep the first error if multiple exist for one field
                ));

        // Add the map of errors to the response body
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailConflict(EmailAlreadyExistsException ex) {
        // 409 Conflict is the standard for duplicate resources
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problemDetail.setTitle("Account Conflict");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setType(URI.create("https://docs.abhijeet-airbnb.com/errors/email-already-registered"));
        problemDetail.setProperty("errorCode", "USER_001"); // Industry tip: use internal error codes for easier debugging

        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex){
        // 1. Created a problem detail for 401 unauthorized
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );

        // 2. Set title for problem detail
        problemDetail.setTitle("Authentication Failed");
        // 3.
        problemDetail.setProperty("timestamp", java.time.Instant.now());
        problemDetail.setType(URI.create("https://docs.abhijeet-airbnb.com/errors/invalid-credentials"));
        return problemDetail;
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("User Not found");
        problemDetail.setType(URI.create("https://docs.abhijeet-airbnb.com/errors/user-not-found"));
        problemDetail.setProperty("timestamp", java.time.Instant.now());
        return problemDetail;
    }
}
