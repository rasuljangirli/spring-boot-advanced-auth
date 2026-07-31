package com.core.identity.controller;

import com.core.identity.dto.request.*;
import com.core.identity.dto.response.ApiResponseDTO;
import com.core.identity.dto.response.LoginResponseDTO;
import com.core.identity.dto.response.RegisterResponseDTO;
import com.core.identity.dto.response.VerifyCodeResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.core.identity.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<RegisterResponseDTO>> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
      RegisterResponseDTO registerResponseDTO =   authService.register(registerRequest);

       return ok(registerResponseDTO,"Qeydiyyat başlandı. 6 rəqəmli təsdiq kodu email ünvanınıza göndərildi.", HttpStatus.CREATED);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponseDTO<VerifyCodeResponseDTO>> verifyCode(@RequestBody @Valid VerifyCodeRequestDTO verifyCodeRequest){
        VerifyCodeResponseDTO verifyCodeResponseDTO = authService.verifyCode(verifyCodeRequest);

        return ok(verifyCodeResponseDTO,"Təbriklər hesabınız təsdiqləndi. Giriş edə bilərsiniz.",HttpStatus.OK);
    }

    @PostMapping("/resend-code")
    public ResponseEntity<ApiResponseDTO<Void>> resendCode(@RequestBody @Valid ResendCodeRequestDTO resendCodeRequest) {
        authService.resendCode(resendCodeRequest);

        return ok(null,"Təsdiq kodu email ünvanınıza göndərildi.",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @RequestBody @Valid LoginRequestDTO request,
            @RequestHeader(value = "User-Agent", required = false, defaultValue = "Unknown Device") String userAgent) {
        LoginResponseDTO response = authService.login(request,userAgent);
        return ok(response, "Giriş uğurla tamamlandı.", HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> refreshToken(
            @RequestBody @Valid RefreshTokenRequestDTO request,
            @RequestHeader(value = "User-Agent", required = false, defaultValue = "Unknown Device") String userAgent) {

        LoginResponseDTO response = authService.refreshToken(request, userAgent);

        return ok(response, "Tokenlər uğurla yeniləndi.", HttpStatus.OK);
    }

}
