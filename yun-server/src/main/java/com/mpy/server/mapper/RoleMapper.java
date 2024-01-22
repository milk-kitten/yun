package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpy.server.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoles(Integer adminID);
}

