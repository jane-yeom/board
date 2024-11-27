package com.board.service;

import com.board.domain.User;
import com.board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;

   public User login(String username, String password) {
        log.info("Login attempt - username: {}", username);

        User user = userMapper.findByUsername(username);

        if (user == null) {
            log.info("User not found: {}", username);
            return null;
        }

        // BCrypt를 사용한 비밀번호 검증
        if (BCrypt.checkpw(password, user.getPassword())) {
            log.info("Login successful for user: {}", username);
            return user;
        } else {
            log.info("Password mismatch for user: {}", username);
            return null;
        }
    }

    public boolean registerUser(User user) {
        // 중복 검사
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return false;
        }

        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        userMapper.save(user);
        return true;
    }
}

