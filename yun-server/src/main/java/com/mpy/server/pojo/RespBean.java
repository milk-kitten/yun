package com.mpy.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RespBean {
    //状态码
    private long code;
    //提示信息
    private String message;
    //可能返回的数据
    private Object obj;

    public RespBean(long code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功返回结果   无数据
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message);
    }

    /**
     * 成功返回结果  有数据
     * @param message
     * @param obj
     * @return
     */
    public static RespBean success(String message,Object obj){
        return new RespBean(200,message,obj);
    }

    /**
     * 失败返回结果 无数据
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message);
    }

    public static RespBean error(String message,Object obj){
        return new RespBean(500,message,obj);
    }


}
