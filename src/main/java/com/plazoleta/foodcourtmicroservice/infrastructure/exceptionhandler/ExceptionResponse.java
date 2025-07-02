package com.plazoleta.foodcourtmicroservice.infrastructure.exceptionhandler;

import java.time.LocalDateTime;

public record ExceptionResponse(String message, LocalDateTime timeStamp) {
}
