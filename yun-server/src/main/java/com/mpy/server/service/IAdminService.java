package com.mpy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Admin;
import com.mpy.server.pojo.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 登陆之后，返回token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户ID获取权限列表
     * @param id
     * @return
     */
    List<Role> getRoles(Integer id);

    List<Admin> getAllAdmins(String keywords);

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    RespBean updatePassword(String oldPass, String pass, Integer adminId);

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}
