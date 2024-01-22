package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpy.server.pojo.Department;
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
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(int i);

    void addDep(Department dep);

    void deleteDep(Department dep);
}
