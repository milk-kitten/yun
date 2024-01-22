package com.mpy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Employee;
import com.mpy.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
public interface IEmployeeService extends IService<Employee> {

    List<Employee> getEmployee(Integer[] ids);

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean insertEmployee(Employee employee);

    /**
     * 获取所有员工（分页）
     * @param currentPage   打开时，默认当前是第几页
     * @param size  每页有多少条数据
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkId();

    /**
     * 获取所有员工套账
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);


}
