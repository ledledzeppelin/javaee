package edu.whu.demo;

import edu.whu.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 在此处从数据库或其他数据源中查找用户信息
        // 这只是一个示例，您需要根据您的数据模型和存储方式进行修改
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 返回Spring Security的UserDetails对象，其中包含用户名、密码和权限
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
