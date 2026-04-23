package com.archive.service;

import com.archive.entity.User;
import com.archive.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    public Page<User> listPage(int page, int size, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword).or().like(User::getRealName, keyword);
        }
        wrapper.orderByDesc(User::getCreatedAt);
        return userMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public void create(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
