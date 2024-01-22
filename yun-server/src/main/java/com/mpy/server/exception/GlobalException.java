package com.mpy.server.exception;

import com.mpy.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * 控制类的增强类：如果发生异常，并且符合类中的自定义的拦截异常类，就会被拦截
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e) {
        if ( e instanceof SQLIntegrityConstraintViolationException ) {
            e.printStackTrace();
            return RespBean.error("该数据有关联数据，操作失败");
        }
        return RespBean.error("数据库异常，操作失败");
    }
}
