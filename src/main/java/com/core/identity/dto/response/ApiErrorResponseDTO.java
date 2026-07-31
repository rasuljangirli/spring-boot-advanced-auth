package com.core.identity.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponseDTO<T> {
    private int statusCode;
    private ExceptionResponseDTO<T> exception;
}
