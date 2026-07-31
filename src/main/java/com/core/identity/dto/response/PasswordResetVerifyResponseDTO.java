package com.core.identity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetVerifyResponseDTO {
    private String message;
    private String resetToken;
}