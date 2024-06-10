package com.example.sst2.dto;

import com.example.sst2.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class WorkerUpdateDto {

    @NotBlank
    @Size(min = 4,max = 30)
    private String name;

    @NotBlank
    @Size(min = 4,max = 30)
    private String password;

    @Min(18)
    @Max(65)
    private int age;

    @Enumerated(EnumType.STRING)
    private Role roles;
}
