package com.core.identity.controller;

import com.core.identity.dto.response.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponseDTO<T>> ok(T data) {
        return ApiResponseDTO.ok(data, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ApiResponseDTO<T>> ok(T data, HttpStatus status) {
        return ApiResponseDTO.ok(data, status);
    }

    protected <T> ResponseEntity<ApiResponseDTO<T>> ok(T data,String message, HttpStatus status) {
        return ApiResponseDTO.ok(data,message, status);
    }

    protected ResponseEntity<ApiResponseDTO<Object>> error(String message, HttpStatus status) {
        return ApiResponseDTO.error(message, status);
    }
}
