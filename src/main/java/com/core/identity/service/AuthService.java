package com.core.identity.service;

import com.core.identity.dto.*;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO registerRequest);

    VerifyCodeResponseDTO verifyCode(VerifyCodeRequestDTO verifyCodeRequest);

    void resendCode(ResendCodeRequestDTO resendCodeRequestDTO);

    LoginResponseDTO login(LoginRequestDTO request, String userAgent);

    LoginResponseDTO refreshToken(RefreshTokenRequestDTO request, String userAgent);
}
