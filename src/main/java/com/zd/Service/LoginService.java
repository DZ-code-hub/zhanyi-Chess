package com.zd.Service;

import com.zd.dto.LoginRequest;
import com.zd.dto.LoginResponse;

public interface LoginService {
     LoginResponse login(LoginRequest loginRequest);
}
