package com.bdmer.framework.base.controller;

import com.alibaba.fastjson.JSON;

import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.common.util.Util;
import com.bdmer.framework.base.dto.CommonResponse;
import com.bdmer.framework.base.dto.PageInfo;
import com.bdmer.framework.base.service.IDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

/**
 * 明细接口
 * DD 明细实体模型
 *
 * @author GongDeLang
 * @since 2019/12/9 17:16
 */
@SuppressWarnings("unchecked")
public interface IDetailController<DD> {
    /**
     * 获取对应的明细Service
     *
     * @return service实例
     */
    IDetailService getDetailService();

    /**
     * 添加明细数据
     *
     * @param data     数据类型
     * @param dataJson 待添加数据json串
     * @return 成功数
     */
    @PostMapping(value = "/batchAddDetail")
    default CommonResponse<Object> batchAddDetail(DD data, String dataJson) {
        List<DD> addEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());

        if (Util.isNull(addEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getDetailService().batchAddDetail(addEntityList);
    }

    /**
     * 更新数据
     *
     * @param data     数据类型
     * @param dataJson 待更新数据json串
     * @return 成功数
     */
    @PostMapping(value = "/batchUpdateDetail")
    default CommonResponse<Integer> batchUpdateDetail(DD data, String dataJson) {
        List<DD> updateEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());
        if (Util.isNull(updateEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getDetailService().batchUpdateDetail(updateEntityList);
    }

    /**
     * 批量删除明细
     *
     * @param ids 待删除的明细id数组
     * @return 成功数
     */
    @PostMapping(value = "/batchDeleteDetail")
    default CommonResponse<Integer> batchDeleteDetail(Long[] ids) {
        if (Util.isNull(ids)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getDetailService().batchDeleteDetail(ids);
    }

    /**
     * 修改明细项目数据(删除，新增，修改)
     *
     * @param deleteIds 待删除数据id
     * @param dataJson  待更新数据json串
     * @param data      数据类型
     * @return 成功数
     */
    @PostMapping(value = "/batchModifyDetail")
    default CommonResponse<Integer> batchModifyDetail(Long[] deleteIds, String dataJson, DD data) {
        List<DD> modifyEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());
        if (Util.isNull(deleteIds) && Util.isNull(modifyEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getDetailService().batchModifyDetail(deleteIds, modifyEntityList);

    }

    /**
     * 查询明细表
     *
     * @param id       主表id
     * @param pageInfo 分页信息
     * @return 明细信息
     */
    @GetMapping(value = "/list/detail")
    default CommonResponse<Object> listDetail(Long id, PageInfo pageInfo) {
        if (Objects.isNull(id)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getDetailService().listDetail(id, pageInfo);
    }
}
