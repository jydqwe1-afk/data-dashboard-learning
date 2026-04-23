package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.User;
import com.archive.security.JwtTokenProvider;
import com.archive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = userService.getByUsername(username);
            String token = tokenProvider.generateToken(username, user.getRole(), user.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("real_name", user.getRealName());
            userInfo.put("role", user.getRole());
            data.put("userInfo", userInfo);
            return Result.ok(data);
        } catch (BadCredentialsException e) {
            return Result.error(401, "用户名或密码错误");
        }
    }

    @GetMapping("/info")
    public Result<?> info(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("real_name", user.getRealName());
        info.put("role", user.getRole());
        info.put("department_id", user.getDepartmentId());
        return Result.ok(info);
    }
}
