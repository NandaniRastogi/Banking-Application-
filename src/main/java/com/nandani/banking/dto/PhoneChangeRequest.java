package com.nandani.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneChangeRequest {

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String number;

}
