package com.mpy.configuration;

import com.mpy.hander.MyAccessDeniedHandle;
import com.mpy.hander.MyAuthenticationFailureHandler;
import com.mpy.hander.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAccessDeniedHandle myAccessDeniedHandle;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单提交认证.
        http.formLogin()
                //设置login.html 表单别名
//                .usernameParameter("username123")
                //自定义登录页面
                .loginPage("/login.html")
                //当发现/login时认为是登录，必须和表单提交的地址是一样的
                //去执行UserServiceImpl
                .loginProcessingUrl("/login")
                /*登录成功后跳转页面，必须是post请求（post像服务器发送数据）
                所以去controller写了跳转页面的方法。
                现在都是前后端分离，不会用controller做跳转了
                做法：创建Hander类，实现AuthenticationSuccessHandler接口
                succesHandler传：    AuthenticationSuccessHandler接口类型
                 */
                /* 登录成功后的处理器，不能和successForwardUrl共存
                forwardurl不能在外跳转。
//                .successForwardUrl("/toMain")
//                .failureForwardUrl("/toError");
                 */
                .successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                .failureHandler(new MyAuthenticationFailureHandler("error.html"));
        //http.authorizeRequests()主要是对url进行访问权限控制，通过这个方法来实现url授权操作。
        http.authorizeRequests()
                .antMatchers("/js/**","/css/**","/images/**").permitAll()
//                .regexMatchers(HttpMethod.POST,"/demo").permitAll()
                //login.html不需要被认证
                .antMatchers("/login.html").permitAll()
                //登录失败的页面不需要被认证
//                .antMatchers("/error.html").permitAll()
                .antMatchers("/error.html").access("permitAll()")
                // hasauthority只有admin1权限才能登录：
//                .antMatchers("/main1.html").hasAnyAuthority("admin1")
                // hasanyauthority只有admin,admin1的权限才能登录。any可以指定多个。
                .antMatchers("/main1.html").hasAnyAuthority("admin1","admin")
//                在配置类中直接写 abc 即可。
                .antMatchers("/main.html").hasRole("abc")
                //通过IP地址判断
                .antMatchers("/main2.html").hasIpAddress("127.0.0.1")
//                所有请求必须都被认证，必须登录后被访问
                // 在yml配置了：所有的URL都需要前面加上xxxx，才能访问
//                .mvcMatchers("/demo").servletPath("/test").permitAll()
//                .antMatchers("/test/demo").permitAll()
//                .antMatchers("/error.html").permitAll()
                /***
                 *  除了上面定义多个antMatchers
                 *  任何请求 必须都被 认证，也就是说必须登录后才能被访问
                 *  ---
                 *  access实现自定义表达式
                 *  做一个demo，判断登录到用户是否有访问当前页面的权限。
                 */
//                .anyRequest().authenticated();
        .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandle);
        //记住我
        http.rememberMe()
                //失效时间：10秒，单位秒
                .tokenValiditySeconds(40)
                //自定义登录逻辑
                .userDetailsService(userDetailsService)
                //持久层对象
                .tokenRepository(persistentTokenRepository);
        http.logout()
        //必须和main.html退出登录的一样("/logout"). 默认就是logout
//                .logoutUrl("/login");
            //跳出去登录页面。
                .logoutSuccessUrl("/login.html");
        //关闭 scrf 防护，类似防火墙
//        http.csrf().disable();


    }

    @Bean
    public PasswordEncoder getPw() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表，第一次启动时需要，第二次启动时注释掉
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}