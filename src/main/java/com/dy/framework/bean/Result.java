package com.dy.framework.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author caiwl
 * 2018-06-08 08:56:16
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Result {
    @NonNull
    private Integer code;
    @NonNull
    private String msg;
    private Object data;
}
