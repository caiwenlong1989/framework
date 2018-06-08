package com.aloestec.framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloestec.framework.bean.Result;
import com.aloestec.framework.form.UpdPwdForm;
import com.aloestec.framework.mapper.UserMapper;
import com.aloestec.framework.model.User;
import com.aloestec.framework.util.JWT;
import com.aloestec.framework.util.MD5;
import com.aloestec.framework.util.ResultUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author caiwl
 * 2018-06-08 09:41:00
 */
@Service
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public Result login(User user) {
        user.setPassword(encodePwd(user.getPassword()));
        
        user = userMapper.selectOne(user);
        if (user == null) return ResultUtil.fail(ResultUtil.Enums.LOGIN.getCode());
        
        Integer id = user.getId();
        userMapper.updLastLogin(id);
        
        return ResultUtil.success(JWT.create(String.valueOf(id)));
    }

    @Transactional
    @Override
    public boolean updPwd(Integer userId, UpdPwdForm form) {
        User user = new User();
        user.setId(userId);
        user.setPassword(encodePwd(form.getPwd()));
        user = userMapper.selectOne(user);
        if (user == null) return false;
        
        userMapper.updPwd(userId, encodePwd(form.getNewPwd()));
        return true;
    }
    
    private String encodePwd(String pwd) {
        String str = MD5.encode(pwd);
        log.info("{} --MD5--> {}", pwd, str);
        return str;
    }

}
