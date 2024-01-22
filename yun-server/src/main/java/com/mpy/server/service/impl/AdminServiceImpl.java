package com.mpy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpy.server.config.utils.AdminUtils;
import com.mpy.server.config.utils.JwtTokenUtil;
import com.mpy.server.mapper.AdminMapper;
import com.mpy.server.mapper.AdminRoleMapper;
import com.mpy.server.mapper.RoleMapper;
import com.mpy.server.pojo.Admin;
import com.mpy.server.pojo.AdminRole;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Role;
import com.mpy.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登录之后 返回token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        //验证码是否为空。或者 比较是否一样，忽略大小写错误
        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码错误，请重新输入");
        }
        //security主要是通过：UserDetailsService里面的username来实现登录的
        //将浏览器传过来的username，放进去。返回的是userDetails用户详细信息(账号、密码、权限等等)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //判断传过来的username是否为空 或者 (浏览器输入的和数据库密码不一致) 则密码或者用户名是错的
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或者密码不正确");
        }
        //判断是否禁用
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用");
        }
        /**
         * 更新security 登录用户对象
         * 参数：userDetails,凭证密码null,权限列表
         *
         * security的全局里面
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //上下文持有人
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        /**
         * 生成token返回给前端
         * 如果以上都没有进入判断，说明用户和密码是正确的，就可以拿到jwt令牌了
         * 根据用户信息生成令牌
         */
        String token = jwtTokenUtil.generateToken(userDetails);
        //有了token，就用map返回
        HashMap<String, String> tokenMap = new HashMap<>();
        //将token返回去
        tokenMap.put("token", token);
        //头部信息也返回去前端，让他放在请求头里面
        tokenMap.put("tokenHead", tokenHead);
        return RespBean.success("登陆成功", tokenMap);
    }

    /**
     * 根据用户名获取对象
     *
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        /**
         * 查询一个（泛型是admin。equals（提示：表的字段”username“：username））
         * 1.用户名去匹配
         * 2.账户是否禁用
         */
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username)
                .eq("enabled", true));
    }

    /**
     * 根据用户id获取权限列表
     *
     * @param adminID
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminID) {
        return roleMapper.getRoles(adminID);
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    /**
     * 更新操作员角色
     *
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        //删除角色
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", adminId));

        this.redisTemplate.delete("menu_" + adminId);

        Integer result = adminRoleMapper.updateAdminRole(adminId, rids);

        if (rids.length == result) {
            //受影响的行数是一样的，说明添加成功了
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @Override
    public RespBean updatePassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (1 == result) {
                return RespBean.success("更新成功！");
            }
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户头像
     *
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (1 == result) {
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            //更新Authentication
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.success("更新成功！", url);
        }
        return RespBean.error("更新失败！");
    }
}
