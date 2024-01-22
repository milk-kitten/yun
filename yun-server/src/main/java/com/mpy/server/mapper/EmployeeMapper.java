package com.mpy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpy.server.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
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
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> getEmployee(Integer[] ids);
    List<Employee> getEmployee(Integer id);

    IPage<Employee> getEmployeeByPage(Page<Employee> page, @Param("employee") Employee employee,@Param("beginDateScope") LocalDate[] beginDateScope);

    IPage<Employee> getEmployeeWithSalary(Page<Employee> page);
}
