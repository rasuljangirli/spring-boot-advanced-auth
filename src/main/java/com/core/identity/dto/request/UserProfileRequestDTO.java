package com.core.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequestDTO {

    @NotBlank(message = "Ad boş ola bilməz.")
    private String firstName;

    @NotBlank(message = "Soyad boş ola bilməz.")
    private String lastName;

    @NotBlank(message = "Telefon nömrəsi boş ola bilməz.")
    private String phoneNumber;
}