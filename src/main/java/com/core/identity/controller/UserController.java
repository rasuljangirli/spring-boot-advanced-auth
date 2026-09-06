package com.core.identity.controller;

import com.core.identity.dto.request.UserProfileRequestDTO;
import com.core.identity.dto.response.ApiResponseDTO;
import com.core.identity.dto.response.UserProfileResponseDTO;
import com.core.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

    private final UserService userService;

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfileRequestDTO requestDTO) {

        String authenticatedEmail = userDetails.getUsername();
        UserProfileResponseDTO responseDTO = userService.updateProfile(authenticatedEmail, requestDTO);

        return ok(responseDTO, "Profil məlumatlarınız uğurla yeniləndi.", HttpStatus.OK);
    }
}