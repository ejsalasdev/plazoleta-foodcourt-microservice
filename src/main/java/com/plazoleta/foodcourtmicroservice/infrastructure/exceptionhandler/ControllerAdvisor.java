package com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementNotFoundException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementFormatException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidElementLengthException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.InvalidOwnerException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.RequiredFieldsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.UnauthorizedOperationException;
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptions.ErrorDecodeAuthoritiesException;
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptions.ErrorDecodeIdException;
import com.plazoleta.foodcourtmicroservice.infrastructure.exceptions.UserNotFoundException;

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

        @ExceptionHandler(ElementNotFoundException.class)
        public ResponseEntity<ExceptionResponse> handleElementNotFoundException(ElementNotFoundException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(ErrorDecodeIdException.class)
        public ResponseEntity<ExceptionResponse> handleErrorDecodeIdException(ErrorDecodeIdException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(ErrorDecodeAuthoritiesException.class)
        public ResponseEntity<ExceptionResponse> handleErrorDecodeAuthoritiesException(
                        ErrorDecodeAuthoritiesException exception) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(InvalidOwnerException.class)
        public ResponseEntity<ExceptionResponse> handleInvalidOwnerException(InvalidOwnerException exception) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(UnauthorizedOperationException.class)
        public ResponseEntity<ExceptionResponse> handleUnauthorizedOperationException(
                        UnauthorizedOperationException exception) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ExceptionResponse(exception.getMessage(), LocalDateTime.now()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception) {
                String message = "An unexpected error occurred. Please contact support if the problem persists.";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ExceptionResponse(message, LocalDateTime.now()));
        }
}
