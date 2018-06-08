package com.aloestec.framework.form;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:22:26
 */
@Data
public class UpdPwdForm {
    @NotBlank
    private String pwd;
    @NotBlank
    private String newPwd;
}
