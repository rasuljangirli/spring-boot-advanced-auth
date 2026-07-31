package com.core.identity.service.impl;
import com.core.identity.dto.request.*;
import com.core.identity.dto.response.LoginResponseDTO;
import com.core.identity.dto.response.RegisterResponseDTO;
import com.core.identity.dto.response.VerifyCodeResponseDTO;
import com.core.identity.exception.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.core.identity.enums.UserRole;
import com.core.identity.enums.UserStatus;
import com.core.identity.enums.VerificationLimit;
import com.core.identity.model.RefreshToken;
import com.core.identity.model.User;
import com.core.identity.model.Verification;
import com.core.identity.repository.VerificationRepository;
import com.core.identity.security.jwt.JwtService;
import com.core.identity.service.AuthService;
import com.core.identity.service.EmailService;
import com.core.identity.service.UserService;
import com.core.identity.util.VerificationCodeUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationRepository verificationRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int VERIFICATION_EXPIRY_SECONDS = VerificationLimit.expirySeconds();


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private final RefreshTokenServiceImpl refreshTokenService;

    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO registerRequest) {

        Optional<User> existingUserOpt = userService.findByEmail(registerRequest.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (existingUser.getStatus() == UserStatus.ACTIVE) {
                throw new EmailAlreadyVerifiedException();
            }

            if (existingUser.getStatus() == UserStatus.PENDING) {

                Optional<Verification> latestVerificationOpt = verificationRepository.findByUser(existingUser);

                if (latestVerificationOpt.isPresent()) {
                    Verification latestVerification = latestVerificationOpt.get();

                    if (latestVerification.getVerificationExpireAt().isBefore(Instant.now())) {
                        updateUserAndSendNewVerification(existingUser, registerRequest);
                        return new RegisterResponseDTO("EMAIL_VERIFICATION", VERIFICATION_EXPIRY_SECONDS);
                    } else {
                        throw new EmailAlreadyExistsException();
                    }
                }
            }
        }

        createNewUserAndSendVerification(registerRequest);

        return new RegisterResponseDTO("EMAIL_VERIFICATION", VERIFICATION_EXPIRY_SECONDS);
    }

    private void saveOrUpdateVerification(User user, String code) {
        Verification verification = verificationRepository.findByUser(user)
                .orElse(new Verification());

        verification.setUser(user);
        verification.setVerificationCode(code);
        verification.setVerificationExpireAt(Instant.now().plusSeconds(VERIFICATION_EXPIRY_SECONDS));
        verification.setLastSentAt(Instant.now());
        verification.setSendCount(1);

        verificationRepository.save(verification);
    }

    private void updateUserAndSendNewVerification(User existingUser, RegisterRequestDTO request) {
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPhoneNumber(request.getPhoneNumber());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userService.save(existingUser);

        String verificationCode = VerificationCodeUtil.generate6DigitCode();
        saveOrUpdateVerification(existingUser, verificationCode);

        emailService.sendEmail(existingUser.getEmail(), "Təsdiq kodunuz:", verificationCode);
    }

    private void createNewUserAndSendVerification(RegisterRequestDTO registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.PENDING);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userService.save(user);

        String verificationCode = VerificationCodeUtil.generate6DigitCode();

        saveOrUpdateVerification(savedUser, verificationCode);

        emailService.sendEmail(savedUser.getEmail(), "Təsdiq kodunuz:", verificationCode);
    }

    @Override
    @Transactional
    public VerifyCodeResponseDTO verifyCode(VerifyCodeRequestDTO verifyCodeRequest){

        User user = userService.findByEmail(verifyCodeRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(VerificationNotFoundException::new);

        if(!verification.getVerificationCode().equals(verifyCodeRequest.getVerificationCode())){
            throw new InvalidVerificationCodeException();
        }

        if(verification.getVerificationExpireAt().isBefore(Instant.now())){
            throw new VerificationExpiredException();
        }

        user.setStatus(UserStatus.ACTIVE);
        userService.save(user);

        VerifyCodeResponseDTO verifyCodeResponseDTO = new VerifyCodeResponseDTO();
        verifyCodeResponseDTO.setEmail(user.getEmail());
        verifyCodeResponseDTO.setStatus(user.getStatus());

        return verifyCodeResponseDTO;
    }

    @Override
    @Transactional(noRollbackFor = {VerificationNotExpiredException.class})
    public void resendCode(ResendCodeRequestDTO resendCodeRequest) {
        User user = userService.findByEmail(resendCodeRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (!user.getStatus().equals(UserStatus.PENDING)) {
            throw new UserAlreadyActiveException();
        }

        Verification verification = verificationRepository.findByUser(user)
                .orElseThrow(VerificationNotFoundException::new);


        if (verification.getSendCount() == VerificationLimit.maxSendCount()
                && Instant.now()
                .minus(VerificationLimit.waitHours(), java.time.temporal.ChronoUnit.HOURS)
                .plusSeconds(VerificationLimit.timeBufferSeconds())
                .isBefore(verification.getLastSentAt())){

            throw new TooManyVerificationAttemptsException();
        }

        if (verification.getSendCount()==VerificationLimit.maxSendCount()
                && verification.getLastSentAt().isBefore(Instant.now()
                .minus(VerificationLimit.waitHours(), java.time.temporal.ChronoUnit.HOURS)
                .plusSeconds(VerificationLimit.timeBufferSeconds())) ){
            verification.setSendCount(0);
        }

        if (verification.getVerificationExpireAt()
                .isBefore(Instant.now().minusSeconds(VerificationLimit.timeBufferSeconds()))) {
            String generatedDigitCode = VerificationCodeUtil.generate6DigitCode();
            emailService.sendEmail(resendCodeRequest.getEmail(),"Təsdiq kodunuz.",generatedDigitCode);
            verification.setVerificationCode(generatedDigitCode);
            verification.setVerificationExpireAt(Instant.now().plusSeconds(VerificationLimit.expirySeconds()));
            verification.setLastSentAt(Instant.now());
            verification.setSendCount(verification.getSendCount()+1);
            verificationRepository.save(verification);

        } else {
               throw new VerificationNotExpiredException();
        }

    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request, String userAgent) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException();
        }

        var userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.saveRefreshToken(user,refreshToken,userAgent);

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional(noRollbackFor = { RefreshTokenExpiredException.class, SuspiciousActivityException.class })
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request, String userAgent) {

        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(RefreshTokenNotFoundException::new);

        User user = refreshTokenEntity.getUser();
        String userEmail = user.getEmail();

        refreshTokenService.verifyExpiration(refreshTokenEntity, userAgent);

        refreshTokenService.deleteByToken(request.getRefreshToken());

        var userDetails = userDetailsService.loadUserByUsername(userEmail);
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.saveRefreshToken(user, newRefreshToken, userAgent);

        return LoginResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}