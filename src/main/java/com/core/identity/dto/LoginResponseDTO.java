package com.core.identity.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
}