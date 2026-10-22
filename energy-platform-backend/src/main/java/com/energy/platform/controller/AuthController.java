package com.energy.platform.controller;


import com.energy.platform.common.Result;
import com.energy.platform.dto.LoginRequest;
import com.energy.platform.service.AuthService;
import com.energy.platform.utils.JwtUtil;
import com.energy.platform.vo.LoginVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final boolean secureCookie;
    public AuthController(AuthService authService, JwtUtil jwtUtil,
                          @Value("${app.cookie.secure:false}") boolean secureCookie) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.secureCookie = secureCookie;
    }
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest,
                                 HttpServletResponse response) {
        LoginVO login = authService.login(request, servletRequest.getRemoteAddr());
        addCookie(response, "ENERGY_ACCESS", jwtUtil.generateAccessToken(login.getUsername()), 15 * 60);
        addCookie(response, "ENERGY_REFRESH", jwtUtil.generateRefreshToken(login.getUsername()), 7 * 24 * 60 * 60);
        login.setToken(null);
        return Result.ok(login);
    }
    @PostMapping("/logout")
    public Result<Boolean> logout(HttpServletResponse response) {
        clearCookie(response, "ENERGY_ACCESS");
        clearCookie(response, "ENERGY_REFRESH");
        return Result.ok(true);
    }
    @PostMapping("/refresh")
    public Result<Map<String,Boolean>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookie(request, "ENERGY_REFRESH");
        if (refreshToken == null || !jwtUtil.isTokenType(refreshToken, "refresh")) {
            throw new com.energy.platform.common.BusinessException(com.energy.platform.common.ErrorCode.UNAUTHORIZED);
        }
        addCookie(response, "ENERGY_ACCESS", authService.refresh(refreshToken), 15 * 60);
        return Result.ok(Map.of("refreshed", true));
    }

    private void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value).httpOnly(true).secure(secureCookie)
            .sameSite("Strict").path("/").maxAge(maxAge).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "").httpOnly(true).secure(secureCookie).sameSite("Strict")
            .path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String cookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
