package com.example.hellow_world.controller;

import com.example.hellow_world.model.User;
import com.example.hellow_world.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hellow_world.service.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController //返回JSON 数据
@RequestMapping("/api")
public class UserController{
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    //客户端发送一个Post请求携带JSON格式的数据，spring把json转换成user对象userService调用并储存
    @PostMapping("/users")
    public ResponseEntity<?> register(@RequestBody User user){
        System.out.println("Received User: " + user);
        try{
            User savedUser = userService.save(user);
            // 阶段2使用JWTUtil 生成 JWT
            String token = jwtUtil.generateToken(savedUser.getEmail());
            //阶段2 返回包含用户信息
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("token", token);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }

    //阶段2 登录接口
    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> login){
        String email = login.get("email");
        String password = login.get("password");

        try{
            //查找这个用户
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
            if(!userService.validatePassword(password, user.getPassword())){
                throw new IllegalArgumentException("Invalid password");
            }

            String token = jwtUtil.generateToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }
    @PostMapping("/users/protected")
    public ResponseEntity<?> protectedUser(){
        return ResponseEntity.ok("protected");
    }
}
/*前端请求
@RestController 控制器类，用于处理HTTP请求， 它返回的数据会直接转换成 JSON 格式，通过网络发送给客户端。
@RequestMapping("/api/users") 定义了这个控制器的基础路径。意思：所有以 /api/users 开头的请求都会
由这个类处理。
@PostMapping Post请求

*/