package com.mpy.service.impl;

import com.mpy.pojo.Department;
import com.mpy.mapper.DepartmentMapper;
import com.mpy.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
