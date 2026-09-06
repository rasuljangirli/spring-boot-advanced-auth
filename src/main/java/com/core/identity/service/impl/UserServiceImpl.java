package com.core.identity.service.impl;

import com.core.identity.dto.request.UserProfileRequestDTO;
import com.core.identity.dto.response.UserProfileResponseDTO;
import com.core.identity.exception.BaseException;
import com.core.identity.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import com.core.identity.model.User;
import com.core.identity.repository.UserRepository;
import com.core.identity.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
       return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserProfileResponseDTO updateProfile(String email, UserProfileRequestDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPhoneNumber(request.getPhoneNumber().trim());

        userRepository.save(user);

        return new UserProfileResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber()
        );
    }


}
