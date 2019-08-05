package com.dy.framework.model;

import javax.persistence.Id;

import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:12:18
 */
@Data
public class Base {
    @Id
    private Integer id;
}
