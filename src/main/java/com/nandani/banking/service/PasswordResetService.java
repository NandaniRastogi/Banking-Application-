package com.nandani.banking.service;

import com.nandani.banking.dto.ForgotPasswordRequest;
import com.nandani.banking.dto.ResetPasswordRequest;

public interface PasswordResetService {

    String generateRandomToken(ForgotPasswordRequest forgotPasswordRequest);

    String validateAndResetPassword(ResetPasswordRequest resetPasswordRequest);

}
