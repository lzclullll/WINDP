package com.energy.platform.service.impl;


import com.energy.platform.common.BusinessException;
import com.energy.platform.dto.LoginRequest;
import com.energy.platform.entity.LoginLog;
import com.energy.platform.entity.User;
import com.energy.platform.mapper.LoginLogMapper;
import com.energy.platform.mapper.UserMapper;
import com.energy.platform.service.AuthService;
import com.energy.platform.security.AuthorizationService;
import com.energy.platform.utils.JwtUtil;
import com.energy.platform.vo.LoginVO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final LoginLogMapper loginLogMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;
    public AuthServiceImpl(JwtUtil jwtUtil, LoginLogMapper loginLogMapper, UserMapper userMapper,
                           PasswordEncoder passwordEncoder, AuthorizationService authorizationService) {
        this.jwtUtil = jwtUtil;
        this.loginLogMapper = loginLogMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }
    @Override
    public LoginVO login(LoginRequest request, String ip) {
        LoginLog log = new LoginLog();
        log.setUsername(request.getUsername()); log.setIp(ip); log.setCreatedAt(LocalDateTime.now());
        try {
            User user = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()).eq(User::getDeleted, 0));
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("用户名或密码错误");
            }
            log.setStatus("SUCCESS"); log.setMessage("登录成功"); loginLogMapper.insert(log);
            return new LoginVO(
                null,
                request.getUsername(),
                user.getRealName(),
                authorizationService.rolesForUser(user.getId()),
                authorizationService.permissionsForUser(user.getId())
            );
        } catch (Exception e) {
            log.setStatus("FAILED"); log.setMessage("用户名或密码错误"); loginLogMapper.insert(log);
            throw new BusinessException("用户名或密码错误");
        }
    }
    @Override
    public String refresh(String token) { return jwtUtil.generateAccessToken(jwtUtil.getUsername(token)); }
}
