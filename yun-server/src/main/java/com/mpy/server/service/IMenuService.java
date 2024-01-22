package com.mpy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpy.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();
    List<Menu> getMenusByAdminId();

    List<Menu> getAllMenus();
}
