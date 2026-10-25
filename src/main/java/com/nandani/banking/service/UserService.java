package com.nandani.banking.service;

import com.nandani.banking.dto.PhoneChangeRequest;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.dto.UpdateEmailRequest;
import com.nandani.banking.dto.UpdateUserProfileRequest;
import com.nandani.banking.dto.UserResponse;
import com.nandani.banking.entitiy.User;

public interface UserService {

    User registerUser(RegisterRequest user);

    UserResponse getUserDetails(String email);

    void UpdateUserDetails(String email, UpdateUserProfileRequest updateUserProfileRequest);

    void UpdateUserEmail(String email, UpdateEmailRequest updateEmailRequest);

    void UpdateUserPhoneNumber(String email, PhoneChangeRequest phoneChangeRequest);

    

}
