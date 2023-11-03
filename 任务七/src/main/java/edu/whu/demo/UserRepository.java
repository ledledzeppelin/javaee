package edu.whu.demo;

import edu.whu.demo.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private Map<String, UserEntity> users = new HashMap<>();

    public UserRepository() {
        // 添加一些示例用户数据
        users.put("user1", new UserEntity("user1", "password1"));
        users.put("user2", new UserEntity("user2", "password2"));
    }

    public UserEntity findByUsername(String username) {
        return users.get(username);
    }
}

