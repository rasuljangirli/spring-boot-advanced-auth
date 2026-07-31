package com.core.identity.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExceptionResponseDTO<T> {
    private String apiAddress;
    private String apiUrl;
    private LocalDateTime createTime;
    private T errorMessage;
}
