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
import java.util.stream.Collectors;

/**
 * 明细服务服务接口
 * DD 明细实体数据模型
 *
 * @author 龚德浪
 * @since 2019/12/4 17:32:45
 */
@SuppressWarnings("unchecked")
public interface IDetailService<DD> {
    /**
     * 获取对应明细Core
     *
     * @return core实例
     */
    ICommonCore getDetailCore();

    /**
     * 记录业务日志
     *
     * @param commonOperateEnum 公共操作方式
     * @param entityList        实体数组
     */
    default void writeDetailLog(CommonOperateEnum commonOperateEnum, List<DD> entityList) {
        if (Util.isNull(commonOperateEnum)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_LOG_DATA_IS_EMPTY);
        }
    }

    /**
     * 是否被引用
     *
     * @param entityList 明细数据
     */
    default void checkIsQuote(List<DD> entityList) {
    }

    /**
     * 批量添加明细
     *
     * @param entityList 实体数组
     * @return 成功结果
     */
    default CommonResponse<Object> batchAddDetail(List<DD> entityList) {
        this.checkIsQuote(entityList);
        Integer successCount = this.getDetailCore().batchAdd(entityList);

        // 成功后记录日志
        try {
            if (successCount > 0) {
                this.writeDetailLog(CommonOperateEnum.ADD, entityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[IDetailService - batchDelete] 记录日志错误：", e);
        }

        return R.success(successCount);
    }

    /**
     * 批量更新明细
     *
     * @param entityList 实体对象
     * @return 成功结果
     */
    default CommonResponse<Object> batchUpdateDetail(List<DD> entityList) {
        this.checkIsQuote(entityList);
        Integer successCount = this.getDetailCore().batchUpdate(entityList);

        // 成功后记录日志
        try {
            if (successCount > 0) {
                this.writeDetailLog(CommonOperateEnum.UPDATE, entityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[IDetailService - batchDelete] 记录日志错误：", e);
        }

        return R.success(successCount);
    }

    /**
     * 批量删除明细
     *
     * @param ids Id数组
     * @return 成功结果
     */
    default CommonResponse<Object> batchDeleteDetail(Long[] ids) {
        if (Util.isNull(ids)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_IS_EMPTY);
        }

        // 判断该id是否存在
        List<DD> entityList = this.getDetailCore().list(ids);
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_ID_DATA_IS_EMPTY);
        }

        // 检查是否被引用
        this.checkIsQuote(entityList);

        Integer result = this.getDetailCore().batchLogicDelete(ids);
        // 成功后记录日志
        try {
            if (result > 0) {
                this.writeDetailLog(CommonOperateEnum.DELETE, entityList);
            }
        } catch (Exception e) {
            LogUtils.logError("[IDetailService - batchDelete] 记录日志错误：", e);
        }


        return R.success(this.getDetailCore().batchLogicDelete(ids));
    }

    /**
     * 批量修改、新增、删除
     *
     * @param deleteIds  待删除id
     * @param entityList 项目内容列表
     * @return 修改、新增、删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    default CommonResponse<Object> batchModifyDetail(Long[] deleteIds, List<DD> entityList) {

        // 批量逻辑删除
        Object deleteResult = null;
        if (deleteIds != null && deleteIds.length > 0) {
            deleteResult = this.batchDeleteDetail(deleteIds).getData();
        }

        // 为空直接返回0
        if (Util.isNull(entityList)) {
            return R.success(0);
        }

        // 获取id
        Field idField = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), "id");
        if (Objects.isNull(idField)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_REFLECTION_FIELD_IS_EMPTY);
        }
        idField.setAccessible(Boolean.TRUE);

        // 新增
        List<DD> addEntityList = entityList.stream().filter(e -> {
            Long value = 1L;
            try {
                value = (Long) idField.get(e);
            } catch (Exception ex) {
                LogUtils.logError(ex);
            }

            return Objects.isNull(value) || (Long) value <= 0;
        }).collect(Collectors.toList());

        Object addResult = null;
        if (!addEntityList.isEmpty()) {
            addResult = this.batchAddDetail(addEntityList).getData();
        }

        // 更新
        List<DD> updateEntityList = entityList.stream().filter(e -> {
            Object value = null;
            try {
                value = idField.get(e);
            } catch (Exception ex) {
                LogUtils.logError(ex);
            }

            return Objects.nonNull(value) && (Long) value > 0;
        }).collect(Collectors.toList());
        idField.setAccessible(Boolean.FALSE);

        Object updateResult = null;
        if (!updateEntityList.isEmpty()) {
            updateResult = this.batchUpdateDetail(updateEntityList).getData();
        }

        // 判断返回值
        Object result = null;
        if (Objects.nonNull(deleteResult)) {
            result = deleteResult;
        }
        if (Objects.isNull(result)) {
            result = addResult;
        }
        if (Objects.isNull(result)) {
            result = updateResult;
        }


        return R.success(result);
    }

    /**
     * 根据主表id获取明细
     *
     * @param id       主表Id
     * @param pageInfo 分页信息
     * @return 明细结果
     */
    default CommonResponse<List> listDetail(Long id, PageInfo pageInfo) {
        return R.success(new ArrayList<>());
    }
}



