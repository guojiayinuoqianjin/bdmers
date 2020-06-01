package com.bdmer.framework.base.controller;

import com.alibaba.fastjson.JSON;
import com.differ.jackyun.framework.component.basic.dto.JackYunResponse;
import com.differ.jackyun.framework.component.basic.dto.PageInfo;
import com.differ.jackyun.framework.component.basic.interceptor.ServiceException;
import com.differ.jackyun.mrp2.common.enums.result.CommonResponseCodesEnum;
import com.differ.jackyun.mrp2.common.util.Util;
import com.differ.jackyun.mrp2.service.IDetailService;
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
    default JackYunResponse<Object> batchAddDetail(DD data, String dataJson) {
        List<DD> addEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());

        if (Util.isNullOrEmpty(addEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_INVALID_PARAM);
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
    default JackYunResponse<Integer> batchUpdateDetail(DD data, String dataJson) {
        List<DD> updateEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());
        if (Util.isNullOrEmpty(updateEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_INVALID_PARAM);
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
    default JackYunResponse<Integer> batchDeleteDetail(Long[] ids) {
        if (Util.isNullOrEmpty(ids)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_INVALID_PARAM);
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
    default JackYunResponse<Integer> batchModifyDetail(Long[] deleteIds, String dataJson, DD data) {
        List<DD> modifyEntityList = (List<DD>) JSON.parseArray(dataJson, data.getClass());
        if (Util.isNullOrEmpty(deleteIds) && Util.isNullOrEmpty(modifyEntityList)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_INVALID_PARAM);
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
    default JackYunResponse<Object> listDetail(Long id, PageInfo pageInfo) {
        if (Objects.isNull(id)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_INVALID_PARAM);
        }

        return this.getDetailService().listDetail(id, pageInfo);
    }
}
