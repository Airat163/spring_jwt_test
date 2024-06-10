package com.example.sst2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class AuthenticationDto {

    @NotBlank
    @Size(min = 4,max = 30)
    private String username;

    @NotBlank
    @Size(min = 4, max = 200)
    private String password;
}
