package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpy.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer updateAdminRole(Integer adminId, Integer[] rids);
}
