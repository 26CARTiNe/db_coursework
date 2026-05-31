package ru.rsatu.Auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String login;
    private String password;
}