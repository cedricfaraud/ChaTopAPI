package com.openclassrooms.ChaTopAPI.controllers.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String login;

    private String password;
}
