package com.mpy.server.controller;


import com.mpy.server.pojo.Admin;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Role;
import com.mpy.server.service.IAdminService;
import com.mpy.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService iAdminService;

    @Autowired
    private IRoleService iRoleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keywords) {
        List<Admin> allAdmins = iAdminService.getAllAdmins(keywords);
        System.out.println(allAdmins);
        return allAdmins;
//        return iAdminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (iAdminService.updateById(admin)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id) {
        if (iAdminService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }



    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return iRoleService.list();
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/role")
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        return iAdminService.updateAdminRole(adminId,rids);
    }
}
