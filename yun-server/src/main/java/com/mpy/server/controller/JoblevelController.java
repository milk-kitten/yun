package com.mpy.server.controller;


import com.mpy.server.pojo.Joblevel;
import com.mpy.server.pojo.RespBean;
import com.mpy.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {

    @Autowired
    private IJoblevelService iJoblevelService;

    /**
     * 查询所有
     * @return
     */
    @ApiOperation(value = "获取所有的职称")
    @GetMapping("/")
    public List<Joblevel> getAllLevel() {
        return iJoblevelService.list();
    }

    /**
     * 添加
     * @param joblevel
     * @return
     */
    @ApiOperation(value = "增加职称信息")
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel) {
        joblevel.setCreateDate(LocalDateTime.now());
        if (iJoblevelService.save(joblevel)) {
            return  RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 更新
     * @param joblevel
     * @return
     */
    @ApiOperation(value = "更改职称信息")
    @PutMapping("/")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel) {
        if (iJoblevelService.updateById(joblevel)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除职称")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id) {
        if (iJoblevelService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除职称")
    @DeleteMapping("/")
    public RespBean deleteJobLevelByIds(Integer[] ids) {
        if (iJoblevelService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }
}
