package com.nandani.banking.service;

import com.nandani.banking.dto.LoginRequest;
import com.nandani.banking.dto.RegisterRequest;
import com.nandani.banking.entitiy.User;

public interface UserService {

    User registerUser(RegisterRequest user);

}
