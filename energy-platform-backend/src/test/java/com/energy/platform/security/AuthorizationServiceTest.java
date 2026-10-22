package com.energy.platform.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(scripts = {"/db/schema.sql", "/db/data.sql"})
class AuthorizationServiceTest {
    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    void loadsAdminRolesAndPermissionsFromDatabase() {
        AuthorizationService service = new AuthorizationService(jdbcTemplate);
        assertThat(service.rolesForUser(1L)).containsExactly("ADMIN");
        assertThat(service.permissionsForUser(1L)).contains("system:manage", "assistant:use", "prediction:manage");
    }

    @Test
    void limitsOperatorPermissions() {
        AuthorizationService service = new AuthorizationService(jdbcTemplate);
        assertThat(service.rolesForUser(4L)).containsExactly("OPERATOR");
        assertThat(service.permissionsForUser(4L)).containsExactly("dashboard:view", "workorder:resolve", "workorder:view")
            .doesNotContain("system:manage", "alarm:manage", "assistant:use", "workorder:manage");
    }
}
