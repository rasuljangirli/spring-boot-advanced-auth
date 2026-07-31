package com.core.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Düzgün email formatı daxil edin")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 8, max = 32, message = "Şifrə ən az 8 ən çox 32 simvol olmalıdır")
    private String password;
}