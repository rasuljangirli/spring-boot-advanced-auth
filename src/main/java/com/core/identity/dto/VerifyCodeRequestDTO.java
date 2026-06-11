package com.core.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyCodeRequestDTO {
    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email daxil edilməlidir")
    private String email;

    @NotBlank(message = "Təsdiq kodu daxil edilməlidir")
    @Pattern(regexp = "\\d{6}", message = "Təsdiq kodu 6 rəqəm olmalıdır")
    private String verificationCode;
}
