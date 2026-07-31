package com.core.identity.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T>ResponseEntity<ApiResponseDTO<T>> ok(T data, HttpStatus status){
        ApiResponseDTO<T> response = new ApiResponseDTO<>(true,"Success",data);
        return ResponseEntity.status(status).body(response);
    }

    public static <T>ResponseEntity<ApiResponseDTO<T>> ok(T data,String message, HttpStatus status){
        ApiResponseDTO<T> response = new ApiResponseDTO<>(true,message,data);
        return ResponseEntity.status(status).body(response);
    }

    public static <T>ResponseEntity<ApiResponseDTO<T>> error(String message,HttpStatus status){
        ApiResponseDTO<T> response = new ApiResponseDTO<>(false,message,null);
        return ResponseEntity.status(status).body(response);
    }
}
