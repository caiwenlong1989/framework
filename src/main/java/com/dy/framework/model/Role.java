package com.dy.framework.model;

import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 13:58:42
 */
@Data
@Table(name = "t_role")
public class Role extends Base {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
}
