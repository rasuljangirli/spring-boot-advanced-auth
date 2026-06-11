package com.core.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Ad daxil edilməlidir")
    private String firstName;

    @NotBlank(message = "Soyad daxil edilməlidir")
    private String lastName;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email daxil edilməlidir")
    private String email;

    @NotBlank(message = "Telefon nömrəsi daxil edilməlidir")
    private String phoneNumber;

    @NotBlank(message = "Şifrə daxil edilməlidir")
    @Size(min = 8, max = 32, message = "Şifrə ən az 8 ən çox 32 simvol ola bilər")
    private String password;
}
