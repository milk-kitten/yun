package com.mpy.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.MenuRole;
import com.mpy.server.service.IMenuRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@RestController
@RequestMapping("/menu-role")
public class MenuRoleController {
    @Autowired
    private IMenuRoleService iMenuRoleService;

    /**
     * 根据角色id查询菜单id
     * @param rid
     * @return
     */
    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        List<MenuRole> rid1 = iMenuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid));
        List<Integer> collect = rid1.stream().map(MenuRole::getMid).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * @param rid   角色id
     * @param mids  菜单id的数组
     * @return
     */
    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        return  iMenuRoleService.updateMenuRole(rid,mids);
    }

}
