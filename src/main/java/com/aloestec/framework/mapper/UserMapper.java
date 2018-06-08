package com.aloestec.framework.mapper;

import java.util.Date;

import com.aloestec.framework.model.User;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:42:00
 */
public interface UserMapper extends Mapper<User> {
    /**
     * 用户每次登录时，修改最后登录时间
     * @param id
     */
    default void updLastLogin(Integer id) {
        User record = new User();
        record.setId(id);
        record.setLastLogin(new Date());
        updateByPrimaryKeySelective(record);
    }
    /**
     * 修改密码
     * @param id
     * @param newPwd 新密码
     */
    default void updPwd(Integer id, String newPwd) {
        User record = new User();
        record.setId(id);
        record.setPassword(newPwd);
        updateByPrimaryKeySelective(record);
    }
}
