package net.in.skylink.attendance.config;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.utils.ErrorResponse;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(net.in.skylink.attendance.exceptions.ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(
            ServiceException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            ex.getErrorCode().toString(),
            ex.getDetails(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, ex.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Internal server error",
            "SERVER_ERROR",
            List.of(ex.getMessage()),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}