package com.programmingtechie.identity_service.exception;

import com.programmingtechie.identity_service.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
//        String enumKey = exception.getFieldError().getDefaultMessage();
//
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//        } catch (IllegalArgumentException e){
//
//        }
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

//    @ExceptionHandler(value = IllegalArgumentException.class)
//    ResponseEntity<ApiResponse> handlingIllegalArgumentException(IllegalArgumentException exception) {
//        ApiResponse apiResponse = new ApiResponse();
//
//        //apiResponse.setCode(99999);
//        apiResponse.setMessage(exception.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
}