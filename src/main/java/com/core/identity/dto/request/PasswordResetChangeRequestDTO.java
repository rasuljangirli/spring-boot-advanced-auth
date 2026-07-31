package com.core.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetChangeRequestDTO {

    @NotBlank(message = "Reset token daxil edilməlidir.")
    private String resetToken;

    @NotBlank(message = "Şifrə daxil edilməlidir")
    @Size(min = 8, max = 32, message = "Şifrə ən az 8 ən çox 32 simvol ola bilər")
    private String newPassword;
}