package com.bdmer.server.tbk.service;

import com.bdmer.server.tbk.core.UserCoreImpl;
import com.bdmer.framework.base.service.ICommonService;
import com.bdmer.framework.base.core.ICommonCore;
import com.bdmer.server.tbk.dto.UserFilterDTO;
import com.bdmer.server.tbk.dto.UserCreateDTO;
import com.bdmer.server.tbk.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户 - SERVICE
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@Component
public class UserServiceImpl implements
        ICommonService<UserFilterDTO, UserCreateDTO, UserEntity> {

    @Autowired
    private UserCoreImpl userCore;

     /**
      * 获取公共core
      *
      * @return 公共core
      */
     @Override
     public ICommonCore getCommonCore() {
         return this.userCore;
     }
}