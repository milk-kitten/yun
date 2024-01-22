package com.mpy.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface MyService {
    /**
     * 允许的以西
     * request:是为了去拿对应的主体和权限
     * authentication:权限的意思
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
