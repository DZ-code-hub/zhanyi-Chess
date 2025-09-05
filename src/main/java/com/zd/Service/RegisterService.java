package com.zd.Service;

import com.zd.dto.RegisterRequest;
import com.zd.dto.RegisterResponse;

public interface RegisterService {
    RegisterResponse Register(RegisterRequest request);
}
