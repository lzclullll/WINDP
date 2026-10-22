package com.energy.platform.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginVO {
    private String token;
    private String username;
    private String realName;
    private List<String> roles;
    private List<String> permissions;
}
