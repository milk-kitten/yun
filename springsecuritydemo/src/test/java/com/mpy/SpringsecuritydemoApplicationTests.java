package com.mpy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringsecuritydemoApplicationTests {

    @Test
    void contextLoads() {
        /**
         * passwordEncoder是接口
         * BCryptPasswordEncoder 是实现接口类
         *  encode方法是：传入一个密码值，然后进行加密，返回一个字符串
         *  mathes方法是：传入原始密码，传入加密后的面膜，返回是否相等
         *
         *  加密后的123是：
         *      $2a$10$dKjBIPKkdjOn6xiODHsPKe45/Rsfib53aW4SROZ44kNF745gV1znS
         *  原始密码是否等于加密密码true
         */
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        String encode = pe.encode("123");
        System.out.println("加密后：" + encode);
        boolean matches = pe.matches("123", encode);
        System.out.println(matches);
    }

}
