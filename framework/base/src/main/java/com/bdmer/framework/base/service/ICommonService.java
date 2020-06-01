package com.bdmer.framework.base.service;

import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.cmomon.enums.CommonOperateEnum;
import com.bdmer.framework.base.cmomon.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.cmomon.util.LogUtils;
import com.bdmer.framework.base.cmomon.util.ReflectionUtil;
import com.bdmer.framework.base.cmomon.util.Util;
import com.bdmer.framework.base.core.ICommonCore;
import com.bdmer.framework.base.dto.CommonResponse;
import com.bdmer.framework.base.dto.PageInfo;
import com.bdmer.framework.base.dto.R;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 公共服务接口
 * F 筛选数据模型
 * D 实体数据模型 注意：如果要正确的使用该框架，其中D一定要基础E
 * E 数据库model模型
 *
 * @author 龚德浪
 * @since 2019/12/4 17:32:45
 */
@SuppressWarnings("unchecked")
public interface ICommonService<F, D, E> {
    /**
     * 获取对应Common Core
     *
     * @return core实例
     */
    ICommonCore getCommonCore();

    /**
     * 记录业务日志
     *
     * @param commonOperateEnum 公共操作方式
     * @param entityList        实体数组
     */
    default void writeCommonLog(CommonOperateEnum commonOperateEnum, List<E> entityList) {
        if (Objects.isNull(commonOperateEnum)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_LOG_DATA_IS_EMPTY);
        }
    }

    /**
     * 查询条件的准备 - FilterDTO
     * 注意：不仅仅对Filter作NPE过滤，最主要的是主表的关联操作在这里实现。
     * 因为主表尽量不要关联查询，因此若有关联查询，建议重写该接口
     *
     * @param filter 查询条件
     * @return true：还要继续查询；false：终止查询
     */
    default boolean checkFilter(F filter) {
        if (Objects.isNull(filter)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_FILTER_IS_EMPTY);
        }

        return true;
    }

    /**
     * 添加
     * 注意：因为add中使用this.batchAdd。为防止事务失效，需要加上@Transactional，
     * 也可以通过新建接口获取子类自己实现，但是这样改动最小，后期若影响性能再单独重写该接口。
     *
     * @param entity 实体
     * @return 成功数
     */
    @Transactional(rollbackFor = Exception.class)
    default CommonResponse<Object> add(D entity) {
        if (Objects.isNull(entity)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }

        List<D> entityList = new ArrayList<>();
        entityList.add(entity);

        // 默认调用批量添加方法
        return this.batchAdd(entityList);
    }

    /**
     * 批量添加
     *
     * @param entityList 实体s
     * @return 成功数
     */
    default CommonResponse<Object> batchAdd(List<D> entityList) {
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }

        Integer result = this.getCommonCore().batchAdd(entityList);

        // 成功后记录日志
        try {
            if (Objects.nonNull(result) && result > 0) {
                this.writeCommonLog(CommonOperateEnum.ADD, (List<E>) entityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[ICommonService - add] 记录日志错误：", e);
        }

        return R.success(result);
    }

    /**
     * 更新
     * 注意：因为add中使用this.batchAdd。为防止事务失效，需要加上@Transactional，
     * 也可以通过新建接口获取子类自己实现，但是这样改动最小，后期若影响性能再单独重写该接口。
     *
     * @param entity 实体
     * @return 成功数
     */
    @Transactional(rollbackFor = Exception.class)
    default CommonResponse<Object> update(D entity) {
        if (Objects.isNull(entity)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }

        List<D> entityList = new ArrayList<>();
        entityList.add(entity);

        // 默认调用批量更新方法
        return this.batchUpdate(entityList);
    }

    /**
     * 批量更新
     *
     * @param entityList 实体s
     * @return 成功数
     */
    default CommonResponse<Object> batchUpdate(List<D> entityList) {
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }

        // 通过反射获取id的值
        Field idField = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), "id");
        if (Objects.isNull(idField)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_IS_EMPTY);
        }
        idField.setAccessible(Boolean.TRUE);

        // 获取idList
        List<Long> idList = new ArrayList<>();
        try {
            for (D e : entityList) {
                idList.add((Long) idField.get(e));
            }
        } catch (IllegalAccessException e) {
            LogUtils.logError(e);
        }
        if (Util.isNull(idList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_IS_EMPTY);
        }
        idField.setAccessible(Boolean.FALSE);

        // 获取旧实体
        List<E> oldEntityList = null;
        try {
            oldEntityList = this.getCommonCore().list(Util.listToArray(idList));
        } catch (Exception e) {
            LogUtils.logError("[ICommonService - batchUpdate] - 获取旧实体出错：", e);
        }

        // 开始更新
        Integer result = this.getCommonCore().batchUpdate(entityList);

        // 成功后记录日志
        try {
            if (Objects.nonNull(result) && result > 0 && !Util.isNull(oldEntityList)) {
                this.writeCommonLog(CommonOperateEnum.UPDATE, oldEntityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[ICommonService - batchUpdate] 记录日志错误：", e);
        }

        return R.success(result);
    }

    /**
     * 批量删除
     *
     * @param ids Id数组
     * @return 成功数
     */
    default CommonResponse<Object> batchDelete(Long[] ids) {
        if (Util.isNull(ids)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_IS_EMPTY);
        }

        // 判断该id是否存在
        List<E> entityList = this.getCommonCore().list(ids);
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_DATA_IS_EMPTY);
        }

        // 开始删除
        Integer result = this.getCommonCore().batchLogicDelete(ids);

        // 成功后记录日志
        try {
            if (Objects.nonNull(result) && result > 0) {
                this.writeCommonLog(CommonOperateEnum.DELETE, entityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[ICommonService - batchDelete] 记录日志错误：", e);
        }

        return R.success(result);
    }

    /**
     * 根据id获取
     *
     * @param id 主键id
     * @return 实体
     */
    default CommonResponse<Object> get(Long id) {
        return R.success(this.getCommonCore().get(id));
    }

    /**
     * 根据条件查询
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter 查询条件
     * @return 实体List
     */
    default CommonResponse<List> listByFilter(F filter) {
        // 查询条件准备
        if (!this.checkFilter(filter)) {
            return R.success(new ArrayList());
        }

        return R.success(this.getCommonCore().listByFilter(filter));
    }

    /**
     * 根据条件统计
     * 描述：与listByFilter配套使用，可以通过withDeleted来统计逻辑删除的数据，默认不统计删除的
     *
     * @param filter 查询条件
     * @return 实体List数量
     */
    default CommonResponse<Integer> countByFilter(F filter) {
        // 查询条件准备
        if (!this.checkFilter(filter)) {
            return R.success(0);
        }

        return R.success(this.getCommonCore().countByFilter(filter));
    }

    /**
     * 获取主页面结果
     * 描述：不会获取删除的数据
     *
     * @param filter   查询条件
     * @param pageInfo 分页信息
     * @return 主页面结果List
     */
    default CommonResponse<List> listMain(F filter, PageInfo pageInfo) {
        if (Objects.isNull(pageInfo)) {
            pageInfo = new PageInfo();
        }

        // 查询条件准备
        if (!this.checkFilter(filter)) {
            return R.success(new ArrayList());
        }

        return R.success(this.getCommonCore().listMain(filter, pageInfo));
    }

    /**
     * 获取主页面结果条数
     *
     * @param filter 查询条件
     * @return 主页面结果数量
     */
    default CommonResponse<Integer> listCount(F filter) {

        // 查询条件准备
        if (!this.checkFilter(filter)) {
            return R.success(0);
        }

        return R.success(this.getCommonCore().listCount(filter));
    }

    /**
     * 统计字段的数量
     * 描述：不会统计删除的数据
     *
     * @param field 对应归类字段
     * @return 字段的数量
     */
    default CommonResponse<List> listFieldCount(String field) {
        return R.success(this.getCommonCore().listFieldCount(field));
    }
}



