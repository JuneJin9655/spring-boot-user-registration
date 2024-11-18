package com.example.hellow_world.repository;

import com.example.hellow_world.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // 根据邮箱查找用户
}
//Optional 防止空指针异常
//继承JPA 他的所有增删改查的作用
// 这个类就是帮助你操作数据库的