package com.zd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private  Long id;
    private String username;
    private String message;
    private Boolean success;
}
