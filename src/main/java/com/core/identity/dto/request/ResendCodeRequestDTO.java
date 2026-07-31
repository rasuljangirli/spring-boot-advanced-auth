package com.core.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendCodeRequestDTO {
    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email daxil edilməlidir")
    private String email;
}
