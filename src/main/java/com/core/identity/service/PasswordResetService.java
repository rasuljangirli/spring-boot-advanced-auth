package com.core.identity.service;

import com.core.identity.dto.request.CompletePasswordResetRequestDTO;
import com.core.identity.dto.request.ResendCodeRequestDTO;
import com.core.identity.dto.request.VerifyCodeRequestDTO;
import com.core.identity.dto.response.PasswordResetTokenResponseDTO;

public interface PasswordResetService {

    void initiatePasswordReset(ResendCodeRequestDTO resendCodeRequestDTO);

    PasswordResetTokenResponseDTO verifyPasswordResetOtp(VerifyCodeRequestDTO requestDTO);

    void completePasswordReset(CompletePasswordResetRequestDTO requestDTO);
}
