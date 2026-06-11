package com.core.identity.service.impl;

import lombok.RequiredArgsConstructor;
import com.core.identity.model.User;
import com.core.identity.repository.UserRepository;
import com.core.identity.service.UserService;
import org.springframework.stereotype.Service;

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
}
