package com.mpy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    PasswordEncoder pw;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询数据库判断用户名是否存在，如果不存在就会抛异常
        if (!"admin".equals(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2.如果存在用户则将查询出来的密码尽心解析，与前端比较。
        // 或者直接把密码放入构造方法中。
        //既然是数据库查询出来的，肯定是加密的密码。

        /**
         * user是实现类，实现了UserDetailsService
         *  new user（前端传过来的用户名，数据库的密码，权限列表）
         *
         *  encode：传进去进行加密！！
         *  真是定义权限：具体有两个权限：一个admin、一个普通权限
         */
        String password = pw.encode("123");
        return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal,ROLE_abc,/main.html,/insert,/delete"));
    }
}
