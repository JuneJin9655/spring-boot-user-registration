package com.example.hellow_world.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {//创建表格
        @UniqueConstraint(columnNames = "email")//email是独一无二的
})
public class User implements UserDetails {
    @Id// 定义主键
    @GeneratedValue(strategy = GenerationType.AUTO) // 主键自增
    private Long id;

    @NotBlank(message = "username can not be empty")
    @Column(nullable = false)//用户名不为空
    private String username;

    @Email(message = "invalid Email")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank(message = "password can not be empty")
    private String password;

    @CreationTimestamp//自动生成创建时间
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Spring Security 需要的方法
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账号是否未过期，返回true表示不过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账号是否未被锁定，返回true表示未锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭据是否未过期，返回true表示不过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账号是否可用，返回true表示可用
        return true;
    }

    @Override
    public String getUsername() {
        return  email; // 使用 email 作为用户名
    }
}

//出现的问题， 数据库一定要刷新，getter 和 setter 可以用lombok
