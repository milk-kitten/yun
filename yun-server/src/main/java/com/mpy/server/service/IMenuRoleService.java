package com.mpy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpy.server.pojo.MenuRole;
import com.mpy.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
