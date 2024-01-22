package com.mpy.server.controller;

import com.mpy.server.config.utils.FastDFSUtils;
import com.mpy.server.pojo.Admin;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.service.IAdminService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 个人信息
 */
@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService iAdminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        //更新成功，重新构建Authentication对象
        if (iAdminService.updateById(admin)) {
            /**
             * 1.用户对象
             * 2.凭证（密码）
             * 3.用户角色
             */
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String,Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return iAdminService.updatePassword(oldPass, pass, adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "头像", dataType = "MultipartFile")})
    @PostMapping("/admin/userface")
    public RespBean updateHrUserFace(MultipartFile file, Integer id, Authentication authentication) {
        //获取上传文件地址
        String[] fileAbsolutePath = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        return iAdminService.updateAdminUserFace(url,id,authentication);
    }
}
