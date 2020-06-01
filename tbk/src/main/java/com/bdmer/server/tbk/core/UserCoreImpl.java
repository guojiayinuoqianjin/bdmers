package com.bdmer.server.tbk.core;

import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.common.util.IdGenUtil;
import com.bdmer.framework.base.common.util.Util;
import com.bdmer.framework.base.core.ICommonCore;
import com.bdmer.framework.base.dao.ICommonDao;
import com.bdmer.server.tbk.dao.UserDao;
import com.bdmer.server.tbk.dto.UserFilterDTO;
import com.bdmer.server.tbk.dto.UserResultDTO;
import com.bdmer.server.tbk.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 用户 - CORE
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@Component
public class UserCoreImpl implements ICommonCore<UserFilterDTO, UserEntity, UserResultDTO> {
    @Autowired
    private UserDao userDao;

     /**
      * 获取对应dao
      *
      * @return 对应dao
      */
     @Override
     public ICommonDao getCommonDao() {
         return this.userDao;
     }

     /**
      * 检查过滤条件
      *
      * @param filter 过滤条件
      * @return 过滤条件
      */
     @Override
     public UserFilterDTO checkFilter(UserFilterDTO filter) {
         return Objects.isNull(filter) ? new UserFilterDTO() : filter;
     }

    /**
     * 添加操作 - 批量数据校验
     *
     * @param entityList 实体List
     */
    @Override
    public void checkDataForAdd(List<UserEntity> entityList) {
        // 先调用（父类）接口的校验
        ICommonCore.super.checkDataForAdd(entityList);

        // TODO 业务代码
        entityList.forEach(e -> {
            // 添加id
            if (Objects.isNull(e.getId())) {
                e.setId(IdGenUtil.getId());
            }

            // 设置默认值
            Util.checkProperty(e);
        });

    }

    /**
     * 修改操作 - 批量数据校验
     *
     * @param entityList 实体List
     */
    @Override
    public void checkDataForUpdate(List<UserEntity> entityList) {
        // 先调用（父类）接口的校验
        ICommonCore.super.checkDataForUpdate(entityList);

        // TODO 业务代码
        for (UserEntity e : entityList) {
            if (Objects.isNull(e.getId())) {
                throw new ServiceException(CommonResponseCodesEnum.WARN_ID_IS_EMPTY);
            }
        }
    }
}