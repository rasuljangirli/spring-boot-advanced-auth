package com.core.identity.dto.response;

import lombok.Data;
import com.core.identity.enums.UserStatus;

@Data
public class VerifyCodeResponseDTO {
    private String email;
    private UserStatus status;
}
