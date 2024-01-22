package com.mpy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpy.server.pojo.Department;
import com.mpy.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    RespBean addDep(Department dep);

    RespBean deleteDep(Integer id);
}
