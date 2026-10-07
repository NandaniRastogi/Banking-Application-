package com.nandani.banking.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.LoginRequest;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.User.Role;
import com.nandani.banking.entitiy.User.UserStatus;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest reqUser) {

        User user = new User();
        user.setFirstName(reqUser.getFirstName());
        user.setLastName(reqUser.getLastName());
        user.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        user.setPhone(reqUser.getPhone());
        user.setEmail(reqUser.getEmail());
        user.setRole(Role.Customer);
        user.setStatus(UserStatus.Active);

        userRepository.save(user);
        return user;

    }

}
