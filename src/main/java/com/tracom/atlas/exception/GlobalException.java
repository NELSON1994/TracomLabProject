package com.tracom.atlas.exception;

import com.tracom.atlas.wrapper.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Calendar;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> errors(Exception e) {
        ErrorResponse response = new ErrorResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(e.getMessage());
        response.setTimestamp(Calendar.getInstance().getTimeInMillis());

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
