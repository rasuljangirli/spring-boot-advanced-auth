package com.core.identity.controller;

import com.core.identity.dto.request.CompletePasswordResetRequestDTO;
import com.core.identity.dto.request.ResendCodeRequestDTO;
import com.core.identity.dto.request.VerifyCodeRequestDTO;
import com.core.identity.dto.response.ApiResponseDTO;
import com.core.identity.dto.response.PasswordResetTokenResponseDTO;
import com.core.identity.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class PasswordResetController extends BaseController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO<Void>> forgotPassword(@Valid @RequestBody ResendCodeRequestDTO requestDTO){

        passwordResetService.initiatePasswordReset(requestDTO);

        return ok(null,"Şifrə sıfırlama kodu email ünvanınıza göndərildi. Zəhmət olmasa poçt qutunuzu yoxlayın.", HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO<PasswordResetTokenResponseDTO>> verifyOtp(
            @Valid @RequestBody VerifyCodeRequestDTO requestDTO) {

        PasswordResetTokenResponseDTO responseDTO = passwordResetService.verifyPasswordResetOtp(requestDTO);

        return ok(responseDTO, "Təsdiq kodu doğrudur. Şifrənizi yeniləyə bilərsiniz.", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<Void>> resetPassword(
            @Valid @RequestBody CompletePasswordResetRequestDTO requestDTO) {

        passwordResetService.completePasswordReset(requestDTO);

        return ok(null, "Şifrəniz uğurla yeniləndi. Yeni şifrənizlə daxil ola bilərsiniz.", HttpStatus.OK);
    }
}
