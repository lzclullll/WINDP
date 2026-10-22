package com.energy.platform.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService {
    private final JdbcTemplate jdbcTemplate;

    public AuthorizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> rolesForUser(Long userId) {
        return jdbcTemplate.queryForList("""
            SELECT r.role_code
            FROM roles r
            JOIN user_roles ur ON ur.role_id = r.id
            WHERE ur.user_id = ? AND r.deleted = 0
            ORDER BY r.id
            """, String.class, userId);
    }

    public List<String> permissionsForUser(Long userId) {
        return jdbcTemplate.queryForList("""
            SELECT DISTINCT p.permission_code
            FROM permissions p
            JOIN role_permissions rp ON rp.permission_id = p.id
            JOIN user_roles ur ON ur.role_id = rp.role_id
            WHERE ur.user_id = ? AND p.deleted = 0
            ORDER BY p.permission_code
            """, String.class, userId);
    }
}
