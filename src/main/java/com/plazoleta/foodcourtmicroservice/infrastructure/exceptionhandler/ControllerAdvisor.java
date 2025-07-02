package com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

        @ExceptionHandler(ElementAlreadyExistsException.class)
        public ResponseEntity<ExceptionResponse> handleElementAlreadyExistsException(
                        ElementAlreadyExistsException exception) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(InvalidElementFormatException.class)
        public ResponseEntity<ExceptionResponse> handleInvalidElementFormatException(
                        InvalidElementFormatException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(RequiredFieldsException.class)
        public ResponseEntity<ExceptionResponse> handleRequiredFieldsException(RequiredFieldsException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(InvalidElementLengthException.class)
        public ResponseEntity<ExceptionResponse> handleInvalidElementLengthException(
                        InvalidElementLengthException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception) {
        //         String message = "An unexpected error occurred. Please contact support if the problem persists.";
        //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        //                         .body(new ExceptionResponse(message, LocalDateTime.now()));
        // }
}
