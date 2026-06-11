package com.core.identity.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import com.core.identity.dto.ApiErrorResponseDTO;
import com.core.identity.dto.ExceptionResponseDTO;
import com.core.identity.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponseDTO<String>> handleBaseException(BaseException ex, HttpServletRequest request){
        ApiErrorResponseDTO<String> errorResponse = new ApiErrorResponseDTO<>();
        ExceptionResponseDTO<String> exceptionResponse = new ExceptionResponseDTO<>();
        exceptionResponse.setCreateTime(LocalDateTime.now());
        exceptionResponse.setApiUrl(request.getRequestURL().toString());
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setApiAddress(request.getRemoteAddr());

        errorResponse.setStatusCode(ex.getStatusCode());
        errorResponse.setException(exceptionResponse);

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO<Map<String,Object>>> handleValidException(MethodArgumentNotValidException ex ,HttpServletRequest request ){
        Map<String, Object> errors = new HashMap<>();
        ApiErrorResponseDTO<Map<String, Object>> errorResponse = new ApiErrorResponseDTO<>();
        ExceptionResponseDTO<Map<String, Object>> exceptionResponse = new ExceptionResponseDTO<>();

        errorResponse.setStatusCode(400);

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();

            if(errors.containsKey(field)) {
                Object existing = errors.get(field);
                if(existing instanceof List<?>) {
                    ((List<String>) existing).add(errorMessage);
                } else {
                    List<String> messages = new ArrayList<>();
                    messages.add((String) existing);
                    messages.add(errorMessage);
                    errors.put(field, messages);
                }
            } else {
                errors.put(field, errorMessage);
            }
        });

        exceptionResponse.setCreateTime(LocalDateTime.now());
        exceptionResponse.setApiUrl(request.getRequestURL().toString());
        exceptionResponse.setApiAddress(request.getRemoteAddr());
        exceptionResponse.setErrorMessage(errors);

        errorResponse.setException(exceptionResponse);

        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO<String>> handleGenericException(Exception ex, HttpServletRequest request) {
        ApiErrorResponseDTO<String> errorResponse = new ApiErrorResponseDTO<>();
        ExceptionResponseDTO<String> exceptionResponse = new ExceptionResponseDTO<>();

        exceptionResponse.setCreateTime(LocalDateTime.now());
        exceptionResponse.setApiUrl(request.getRequestURL().toString());
        exceptionResponse.setApiAddress(request.getRemoteAddr());
        exceptionResponse.setErrorMessage(ex.getMessage());

        errorResponse.setStatusCode(500);
        errorResponse.setException(exceptionResponse);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDTO<String>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ApiErrorResponseDTO<String> errorResponse = new ApiErrorResponseDTO<>();
        ExceptionResponseDTO<String> exceptionResponse = new ExceptionResponseDTO<>();

        exceptionResponse.setCreateTime(LocalDateTime.now());
        exceptionResponse.setApiUrl(request.getRequestURL().toString());
        exceptionResponse.setApiAddress(request.getRemoteAddr());
        exceptionResponse.setErrorMessage("Email və ya şifrə yanlışdır.");

        errorResponse.setStatusCode(401);
        errorResponse.setException(exceptionResponse);

        return ResponseEntity.status(401).body(errorResponse);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponseDTO<String>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ApiErrorResponseDTO<String> errorResponse = new ApiErrorResponseDTO<>();
        ExceptionResponseDTO<String> exceptionResponse = new ExceptionResponseDTO<>();

        exceptionResponse.setCreateTime(LocalDateTime.now());
        exceptionResponse.setApiUrl(request.getRequestURL().toString());
        exceptionResponse.setApiAddress(request.getRemoteAddr());
        exceptionResponse.setErrorMessage("Bu əməliyyatı yerinə yetirmək üçün icazəniz yoxdur.");

        errorResponse.setStatusCode(403);
        errorResponse.setException(exceptionResponse);

        return ResponseEntity.status(403).body(errorResponse);
    }

}
