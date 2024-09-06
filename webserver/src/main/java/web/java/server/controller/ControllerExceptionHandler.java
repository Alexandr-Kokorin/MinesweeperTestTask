package web.java.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import web.java.server.controller.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorResponse> badRequestException(HttpClientErrorException.BadRequest e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getStatusText()));
    }
}
