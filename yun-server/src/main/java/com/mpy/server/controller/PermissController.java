package com.mpy.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpy.server.pojo.Menu;
import com.mpy.server.pojo.MenuRole;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Role;
import com.mpy.server.service.IMenuRoleService;
import com.mpy.server.service.IMenuService;
import com.mpy.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/basic/permission")
public class PermissController {
    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private IMenuRoleService iMenuRoleService;

    /**
     * 获取所有角色
     * @return
     */
    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRole(){
        return iRoleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        //由于security遵循必须有role_开头，判断插入的数据如果没有role_开头，则自动添加前缀
        if (!role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }
        if (iRoleService.save(role)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid) {
        if (iRoleService.removeById(rid)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "查询所有的菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return iMenuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return iMenuRoleService.list(new QueryWrapper<MenuRole>()
                        .eq("rid",rid))
                .stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids){
        return iMenuRoleService.updateMenuRole(rid,mids);
    }
}
