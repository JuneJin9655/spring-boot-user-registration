package com.example.hellow_world.service;

import com.example.hellow_world.model.User;
import com.example.hellow_world.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //阶段二findEmail方法
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User save(User user) {
        System.out.println("Password before encoding: " + user.getPassword());//日志测试
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Password after encoding: " + user.getPassword());//日志测试
        return userRepository.save(user);
    }
    //阶段2 raw明文密码 encode加密密码
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}

//连接controller 前端请求 和 数据库的中间人 检查用户名是否存在啊。密码符不符合规则等等
