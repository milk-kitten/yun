package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpy.server.pojo.Admin;
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
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAllAdmins(Integer id, String keywords);
}
