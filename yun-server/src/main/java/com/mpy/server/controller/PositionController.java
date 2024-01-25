package com.mpy.server.controller;


import com.mpy.server.pojo.RespBean;
import com.mpy.server.pojo.Position;
import com.mpy.server.service.IPositionService;
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
@RequestMapping("/system/basic/pos")
public class PositionController {

    //自动注入service
    @Autowired
    private IPositionService iPositionService;

    /**
     * 获取所有职位信息
     * 单表的查询用mybatis plus 直接写完controller即可
     * 调用serice层即可
     * @return
     */
    @ApiOperation(value = "获取所有职位信息")
    @GetMapping("/")
    public List<Position> getAllPositions(){
        return iPositionService.list();
    }

    /**
     * 添加
     * @param position
     * @return
     */
    @ApiOperation(value = "添加职位信息")
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        //职位信息表有：自增id、名字、创建时间、是否启用
        position.setCreateDate(LocalDateTime.now());
        if (iPositionService.save(position)) {
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    /**
     * 更新
     * @param position
     * @return
     */
    @ApiOperation(value = "更改职位信息")
    @PutMapping("/")
    public RespBean updatePosition(@RequestBody Position position) {
        if (iPositionService.updateById(position)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除职位信息")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(@PathVariable Integer id) {
        System.out.println("------------------------------");
        if (iPositionService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "批量删除职位信息")
    @DeleteMapping("/")
    public RespBean deletePositionsByIds(Integer[] ids) {
        if (iPositionService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("批量删除成功");
        }
        return RespBean.error("批量删除失败");
    }

}
