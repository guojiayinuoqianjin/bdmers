package com.bdmer.server.tbk.controller;

import com.bdmer.server.tbk.service.UserServiceImpl;
import  com.bdmer.framework.base.controller.ICommonController;
import com.bdmer.server.tbk.dto.UserFilterDTO;
import com.bdmer.server.tbk.dto.UserCreateDTO;
import  com.bdmer.framework.base.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户 - CONTROLLER
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@RestController
@RequestMapping(value = "/tbk/user", produces = {"application/json;charset=utf-8"})
public class UserController implements
        ICommonController<UserFilterDTO, UserCreateDTO> {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 获取对应的公共服务实例
     *
     * @return 返回对应服务实例
     */
    @Override
    public ICommonService getCommonService() {
        return this.userService;
    }
}