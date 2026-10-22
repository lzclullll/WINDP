package com.energy.platform.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.energy.platform.entity.User;
import com.energy.platform.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final AuthorizationService authorizationService;

    public UserDetailsServiceImpl(UserMapper userMapper, AuthorizationService authorizationService) {
        this.userMapper = userMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getUsername, username)
            .eq(User::getDeleted, 0));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new LoginUser(
            user,
            authorizationService.rolesForUser(user.getId()),
            authorizationService.permissionsForUser(user.getId())
        );
    }
}
