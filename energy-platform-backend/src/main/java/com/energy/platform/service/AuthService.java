package com.energy.platform.service;


import com.energy.platform.dto.LoginRequest;
import com.energy.platform.vo.LoginVO;

public interface AuthService {
    LoginVO login(LoginRequest request, String ip);
    String refresh(String token);
}
