package com.mpy.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.mpy.server.pojo.*;
import com.mpy.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author M
 * @since 2023-12-11
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService iEmployeeService;

    @Autowired
    private INationService iNationService;

    @Autowired
    private IPoliticsStatusService iPoliticsStatusService;

    @Autowired
    private IDepartmentService iDepartmentService;

    @Autowired
    private IJoblevelService iJoblevelService;

    @Autowired
    private IPositionService iPositionService;

    @ApiOperation(value = "获取所有的员工（分页）")
    @GetMapping("/")
    /**
     * currentPage:打开时，默认当前是第几页
     * size，每页有多少条数据
     */
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    Employee employee, LocalDate[] beginDateScope) {
        return iEmployeeService.getEmployeeByPage(currentPage, size, employee, beginDateScope);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus() {
        return iPoliticsStatusService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return iNationService.list();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getAllJobLevels() {
        return iJoblevelService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPositions() {
        return iPositionService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartments() {
        return iDepartmentService.list();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        return iEmployeeService.maxWorkId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee) {
        return iEmployeeService.insertEmployee(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (iEmployeeService.updateById(employee)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id) {
        if (iEmployeeService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response) throws UnsupportedEncodingException {
        //获取当前员工的数据-具体的数据
        List<Employee> list = iEmployeeService.getEmployee(null);

        /**
         * 导出的参数：
         *      导出员工的数据：（文件名的名字，sheet表名，excel的版本）
         *  excel：
         *      03：HSSF - 提供读写Microsoft Excel XLS 格式档案的功能。03打不开07
         *      07：XSSF - 提供读写Microsoft Excel OOXML XLSX格式档案的功能。07能打开03
         *
         *    03的HSSF比07的XSSF优点：
         *      1.速度快
         *      2。兼容性好。03打不开07，07能打开03
         */
        ExportParams params = new ExportParams("员工表", "sheet员工表", ExcelType.HSSF);
        /**
         * ExcelExportUtil导出工具类：（导出的参数、对象类名.class、具体的数据）
         *  返回的是Workbook.是poi的一个类。相当于工作簿（工作簿就是一个Excel）
         */
        Workbook book = ExcelExportUtil.exportExcel(params, Employee.class, list);

        /**
         * response.输出流形式 输出workbook
         *
         * 导出需要一些请求头的响应信息
         * 防止中文乱码
         */
        ServletOutputStream out = null;
        try {
            //流形式，设置一个头
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("员工表.xls", "UTF-8"));
            out = response.getOutputStream();
            book.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file) {
        //创建导入对象
        ImportParams params = new ImportParams();
        //去掉标题第一行
        params.setTitleRows(1);
        /**
         * 导入 流的形式、POJO对象类的字节码、数据--- 返回的是数据
         *
         * 导入完成后，pojo.Employee-nation是有值的
         * pojo.nation拿到的就是name 是因为：
         *  Excel注解，ExcelEntiy注解：
         *      能通过汉族名字获取到它是nation.ExcelEntiy就知道了是对象里面的，从而得到name
         *  如何获得name的id呢？
         *
         *      for循环
         *      重写了hashcode方法，也准备了有参构造。
         *      1.从excel导入的数据中，拿到了nation对象，对象里面有name，id是空的
         *      2.查询所有的nationList,获取对象索引下标：重写了equals和hashcode，
         *          通过name名字去nationList比较。能获取一样的对象.从而得到下标。
         *      3.通过nationList.get(下标).getId() 获取完整的对象：有id，name\
         *      4.在从id，name中获取id。 将id放入到employee中。
         *
         *  if插入：saveBatch批量插入。 插入集合
         *
         */
        List<Nation> nationlList = iNationService.list();
        List<PoliticsStatus> politicsStatusList = iPoliticsStatusService.list();
        List<Department> departmentList = iDepartmentService.list();
        List<Joblevel> joblevelList = iJoblevelService.list();
        List<Position> positionList = iPositionService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                //民族id
                employee.setNationId(nationlList.get(nationlList.indexOf(
                        new Nation(employee.getNation().getName()))).getId());
                //政治面貌id
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(
                        new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                //部门id
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(new
                        Department(employee.getDepartment().getName()))).getId());
                //职称id
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(new
                        Joblevel(employee.getJoblevel().getName()))).getId());
                //职位id
                employee.setPosId(positionList.get(positionList.indexOf(new
                        Position(employee.getPosition().getName()))).getId());
            });
            if (iEmployeeService.saveBatch(list)) {
                return RespBean.success("导入成功!");
            }
            return RespBean.error("导入失败!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败!");
    }

}
