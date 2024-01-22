package com.mpy.server.controller;


import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Salary;
import com.mpy.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    private ISalaryService iSalaryService;

    @ApiOperation(value = "获取所有工资套账")
    @GetMapping("/")
    public List<Salary> getAllSalary() {
        return iSalaryService.list();
    }

    @ApiOperation(value = "添加工资账套")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary) {
        salary.setCreateDate(LocalDateTime.now());
        if (iSalaryService.save(salary)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除工资套账")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id) {
        if (iSalaryService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "更改工资账套")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary) {
        if (iSalaryService.updateById(salary)) {
            return RespBean.success("更改成功！");
        }
        return RespBean.error("更改失败");
    }
}
