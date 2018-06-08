package com.aloestec.framework.model;

import java.util.Date;

import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:14:49
 */
@Data
@Table(name = "t_user")
public class User extends Base {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Date lastLogin;
}
