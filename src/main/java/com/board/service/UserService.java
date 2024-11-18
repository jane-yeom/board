package com.board.service;

import com.board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.board.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean registerUser(User user) {
        // 중복 검사
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return false;
        }
        if (userMapper.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserYn(true);

        userMapper.save(user);
        return true;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}
