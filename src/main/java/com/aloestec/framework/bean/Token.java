package com.aloestec.framework.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:00:51
 */
@Data
@AllArgsConstructor
public class Token {
    /** 访问Token */
    private String accessToken;
    /** 刷新Token，访问Token过期时使用 */
    private String refreshToken;
    /** Token类型 */
    private String type;
}
