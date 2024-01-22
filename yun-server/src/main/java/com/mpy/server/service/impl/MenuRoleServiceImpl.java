package com.mpy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.mapper.MenuRoleMapper;
import com.mpy.server.pojo.MenuRole;
import com.mpy.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     * @param rid   角色id
     * @param mids  菜单id的数组
     * @return
     */
    @Override
    //事务的注解
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        //删除菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //添加菜单
        if (null == mids || 0 == mids.length) {
            return RespBean.success("更新成功");
        }
        Integer integer = menuRoleMapper.insertRecord(rid,mids);
        if (integer == mids.length) {
            return  RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
