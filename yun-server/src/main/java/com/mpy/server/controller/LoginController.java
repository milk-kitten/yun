package com.mpy.server.controller;

import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Admin;
import com.mpy.server.pojo.AdminLoginParam;
import com.mpy.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api(tags = "LoginController")
@RestController
public class LoginController {
    //注入service
    @Autowired
    private IAdminService adminService;

    //用swagger注解代表注释了
    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        //service层login登录方法
         return adminService.login(
                 adminLoginParam.getUsername(),
                 adminLoginParam.getPassword(),
                 adminLoginParam.getCode(),
                 request);
    }

    /**
     * 获取当前登录的用户信息
     *
     * springsecurity将当前登录的对象设置到全局里面了：
     * SecurityContextHolder.getContext().setAuthentication(authentication);
     * 可以通过principal获取当前登录的对象
     * @param principal
     * @return
     */
    @ApiOperation(value = "获取当前用户登录的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if (null == principal){
            return null;
        }
        //principal 直接获取当前登录的对象
        String username = principal.getName();  //获取当前登录的用户名
        //根据用户名获取完全的用户对象
        Admin admin = adminService.getAdminByUserName(username);
        //但是用户的密码不会返回给浏览器
        admin.setPassword(null);
        //获取用户信息，并将角色也一起返回。角色表时角色表，没有在用户表当中
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功");
    }
}
