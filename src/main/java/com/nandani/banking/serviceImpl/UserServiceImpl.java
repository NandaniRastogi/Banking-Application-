package com.nandani.banking.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nandani.banking.dto.AdminUserResponse;
import com.nandani.banking.dto.PhoneChangeRequest;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.dto.UpdateEmailRequest;
import com.nandani.banking.dto.UpdateUserProfileRequest;
import com.nandani.banking.dto.UserResponse;
import com.nandani.banking.entitiy.User;
import com.nandani.banking.entitiy.User.Role;
import com.nandani.banking.entitiy.User.UserStatus;
import com.nandani.banking.exception.UserAlreadyExistsException;
import com.nandani.banking.exception.UserNotFoundException;
import com.nandani.banking.repository.UserRepository;
import com.nandani.banking.service.PhoneNumberOtpService;
import com.nandani.banking.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelmapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpServiceImpl otpServiceImpl;
    private final PhoneNumberOtpService phoneNumberOtpService;

    @Override
    public User registerUser(RegisterRequest reqUser) {

        User user = new User();
        if (userRepository.existsByEmail(reqUser.getEmail())) {
            throw new UserAlreadyExistsException("User Already Exists exception");
        }
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

    @Override
    public UserResponse getUserDetails(String email) {

        UserResponse userResponse = new UserResponse();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username Password Not Found Exception"));

        modelmapper.map(user, userResponse);

        return userResponse;

    }

    @Transactional
    @Override
    public void UpdateUserDetails(String email, UpdateUserProfileRequest updateUserProfileRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username Password Not Found Exception"));

        user.setFirstName(updateUserProfileRequest.getFirstName());
        user.setLastName(updateUserProfileRequest.getLastName());

        userRepository.save(user);

    }

    @Override
    public void UpdateUserEmail(String email, UpdateEmailRequest updateEmailRequest) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username Password Not Found Exception"));

        otpServiceImpl.generateAndSendOtp(updateEmailRequest.getEmail());

    }

    @Override
    public void UpdateUserPhoneNumber(String email, PhoneChangeRequest phoneChangeRequest) {

        // TODO Auto-generated method stub

        phoneNumberOtpService.requestPhoneChange(email, phoneChangeRequest.getNumber());

        throw new UnsupportedOperationException("Unimplemented method 'UpdateUserPhoneNumber'");
    }

}
