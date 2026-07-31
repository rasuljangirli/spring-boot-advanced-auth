package com.core.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletePasswordResetRequestDTO {

    @NotBlank(message = "Token boş ola bilməz.")
    private String resetToken;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 8, max = 32, message = "Şifrə ən az 8 ən çox 32 simvol olmalıdır")
    private String newPassword;
}