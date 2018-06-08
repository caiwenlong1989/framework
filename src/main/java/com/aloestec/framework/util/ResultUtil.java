package com.aloestec.framework.util;

import com.aloestec.framework.bean.Result;

/**
 * 
 * @author caiwl
 * 2018-06-08 08:56:20
 */
public class ResultUtil {

    public static Result success() {
        return new Result(Enums.SUCCESS.getCode(), Enums.SUCCESS.getMsg());
    }
    
    public static Result success(Object data) {
        return new Result(Enums.SUCCESS.getCode(), Enums.SUCCESS.getMsg(), data);
    }

    public static Result fail() {
        return new Result(Enums.FAIL.getCode(), Enums.FAIL.getMsg());
    }

    public static Result fail(Integer code) {
        return new Result(code, Enums.getMsg(code));
    }

    public static enum Enums {
        /** 操作成功 */
        SUCCESS(0, "操作成功"),
        /** 操作失败 */
        FAIL(1, "操作失败"),
        
        AUTHORIZATION_NULL(100001, "Authorization is null"),
        ACCESS_TOKEN_ERROR(100002, "Access_Token错误"),
        ACCESS_TOKEN_EXPIRE(100003, "Access_Token过期"),
        REFRESH_TOKEN_ERROR(100004, "Refresh_Token错误"),
        REFRESH_TOKEN_EXPIRE(100005, "Refresh_Token过期"),
        
        LOGIN(110001, "用户名或密码错误"),
        ;
        Integer code;
        String msg;
        Enums(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        public Integer getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public static String getMsg(Integer code) {
            for (Enums value : values()) {
                if (value.getCode().equals(code)) {
                    return value.getMsg();
                }
            }
            return FAIL.getMsg();
        }
    }
}
