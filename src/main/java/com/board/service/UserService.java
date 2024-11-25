package com.board.service;

import com.board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.board.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

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
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setUserYn(true);

        userMapper.save(user);
        return true;
    }

    public User login(String username, String password){
        User user = userMapper.findByUsername(username);
        if(user != null && BCrypt.checkpw(password, user.getPassword())){
            return user;
        }
        return null;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
}
