package com.nandani.banking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class PhoneOtpRequest {

    @NotBlank
    private String phone;
    @NotBlank
    private String Otp;

}
