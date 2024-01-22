package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpy.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Mapper
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    Integer insertRecord(@Param("rid") Integer rid,@Param("mids") Integer[] mids);
}
