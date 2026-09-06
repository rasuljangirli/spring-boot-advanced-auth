package com.core.identity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileResponseDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}