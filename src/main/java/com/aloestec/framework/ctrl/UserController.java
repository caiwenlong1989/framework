package com.aloestec.framework.ctrl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloestec.framework.bean.Result;
import com.aloestec.framework.cfg.Constants;
import com.aloestec.framework.form.UpdPwdForm;
import com.aloestec.framework.model.User;
import com.aloestec.framework.service.IUserService;
import com.aloestec.framework.util.ResultUtil;

/**
 * 
 * @author caiwl
 * 2018-06-08 08:56:12
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@Valid User user) {
        return userService.login(user);
    }

    @PostMapping("/updPwd")
    public Result updPwd(@RequestAttribute(Constants.USER_ID) Integer userId, @Valid UpdPwdForm form) {
        boolean success = userService.updPwd(userId, form);
        if (success) return ResultUtil.success();
        else return ResultUtil.fail();
    }
}
