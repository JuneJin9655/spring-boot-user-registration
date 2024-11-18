package com.example.hellow_world.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {//创建表格
        @UniqueConstraint(columnNames = "email")//email是独一无二的
})
public class User {
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

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {

    }
}

//出现的问题， 数据库一定要刷新，getter 和 setter 可以用lombok
