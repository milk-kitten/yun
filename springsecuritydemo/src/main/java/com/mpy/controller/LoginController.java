package com.mpy.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Secured("ROLE_abc")
    @RequestMapping({"/toMain"})
    public String toMain(){
        System.out.println("登录成功。。。。");
        // 重定向到主页
        return "redirect:/main.html";
    }

    @PreAuthorize("hasRole('ROLE_abc')")
    @RequestMapping("/toMain1")
    public String toMain1(){
        return "redirect:/main1.html";
    }

    @RequestMapping("/toError")
    public String toError(){
        System.out.println("登录失败。。。。。");
        return "redirect:/error.html";
    }

    @GetMapping("/demo")
    @ResponseBody
    public String demo(){
        return "demo";
    }


}

