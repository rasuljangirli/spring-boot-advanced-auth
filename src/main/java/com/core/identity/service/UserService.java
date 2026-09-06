package com.core.identity.service;

import com.core.identity.dto.request.UserProfileRequestDTO;
import com.core.identity.dto.response.UserProfileResponseDTO;
import com.core.identity.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    User save(User user);

    UserProfileResponseDTO updateProfile(String email, UserProfileRequestDTO request);
    
}
