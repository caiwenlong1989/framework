package com.dy.framework.service;

import com.dy.framework.bean.Result;
import com.dy.framework.form.UpdPwdForm;
import com.dy.framework.model.User;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:14:54
 */
public interface IUserService {
    /**
     * 登录
     * @param user
     * @return
     */
    Result login(User user);
    /**
     * 修改密码
     * @param userId
     * @param form
     * @return
     */
    boolean updPwd(Integer userId, UpdPwdForm form);
}
