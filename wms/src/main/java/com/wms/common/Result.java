package com.wms.common;

import lombok.Data;

/**
 * 返给前端数据的封装(提醒前端)
 * code:200
 * msg:success
 * total:1
 * data:[
 *
 *   ]
 *
 * @author tk
 * @creats 2023-05-12 21:30
 */
@Data
public class Result {
    private int code;//编码：200 、400
    private String msg;//成功、失败
    private Long total;//总数
    private Object data;//数据

    public static Result fail(){
        return result(400,"fail",0L,null);
    }

    public static Result success(){
        return result(200,"success",0L,null);
    }
    public static Result success(Object data){
        return result(200,"success",0L,data);
    }
    public static Result success(Object data,Long total){
        return result(200,"success",total,data);
    }
    public static Result result(int code, String msg, Long total, Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        result.total = total;
        return result;
    }

}