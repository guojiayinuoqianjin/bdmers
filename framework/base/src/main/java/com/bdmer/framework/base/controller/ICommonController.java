package com.bdmer.framework.base.controller;

import com.alibaba.fastjson.JSON;
import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.common.util.LogUtils;
import com.bdmer.framework.base.common.util.ReflectionUtil;
import com.bdmer.framework.base.common.util.Util;
import com.bdmer.framework.base.dto.CommonResponse;
import com.bdmer.framework.base.dto.PageInfo;
import com.bdmer.framework.base.dto.R;
import com.bdmer.framework.base.service.ICommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 公共基本路由
 * F 筛选模型
 * D 实体模型
 *
 * @author GongDeLang
 * @since 2019/12/9 17:16
 */
@SuppressWarnings("unchecked")
public interface ICommonController<F, D> {

    /**
     * 获取对应的Common Service实例
     *
     * @return service实例
     */
    ICommonService getCommonService();

    /**
     * 实体参数校验
     *
     * @param data 待解析的查询json串
     */
    default void checkD(D data) {
        if (Objects.isNull(data)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }
    }

    /**
     * 过滤条件参数校验
     *
     * @param filter 待解析的查询json串
     */
    default String[] checkF(F filter, String[] cols) {
        // 开始格式化参数
        cols = Util.formatCols(cols);

        if (Objects.isNull(filter)) {
            return cols;
        }

        // 兼容不同的cols:["id","name"...]类型的传参。把[、]、"等符号去掉
        Field colsField = ReflectionUtil.getFiledByName(filter.getClass(), "cols");
        if (Objects.isNull(colsField)) {
            return cols;
        }

        colsField.setAccessible(Boolean.TRUE);

        String[] filterCols = null;
        try {
            filterCols = (String[]) colsField.get(filter);
        } catch (IllegalAccessException e) {
            LogUtils.logError("过滤条件参数校验失败：e", e);
        }

        // 开始格式化参数
        filterCols = Util.formatCols(filterCols);

        String[] newFilterCols;
        if (Util.isNull(filterCols)) {
            newFilterCols = cols;
        } else {
            newFilterCols = filterCols;
        }

        try {
            colsField.set(filter, newFilterCols);
        } catch (IllegalAccessException e) {
            LogUtils.logError("过滤条件参数校验失败：e", e);
        }

        colsField.setAccessible(Boolean.FALSE);

        return cols;
    }

    /**
     * 添加数据
     *
     * @param data     带添加数据
     * @param dataJson 待添加数据json串
     * @return 成功数
     */
    @PostMapping(value = "/add")
    default CommonResponse<Object> add(D data, String dataJson) {
        if (Util.isString(dataJson)) {
            data = (D) JSON.parseObject(dataJson, data.getClass());
        }

        // 数据校验
        this.checkD(data);

        return this.getCommonService().add(data);
    }

    /**
     * 批量添加数据
     *
     * @param dataJson 待添加数据json串
     * @return 成功数
     */
    @PostMapping(value = "/batchAdd")
    default CommonResponse<Object> batchAdd(String dataJson, D entity) {
        List<D> data = (List<D>) JSON.parseArray(dataJson, entity.getClass());

        // 数据校验
        for (D e : data) {
            this.checkD(e);
        }

        return this.getCommonService().batchAdd(data);
    }

    /**
     * 更新数据
     *
     * @param data     数据
     * @param dataJson 待更新数据json串
     * @return 成功数
     */
    @PostMapping(value = "/update")
    default CommonResponse<Object> update(D data, String dataJson) {
        if (Util.isString(dataJson)) {
            data = (D) JSON.parseObject(dataJson, data.getClass());
        }

        // 数据校验
        this.checkD(data);

        return this.getCommonService().update(data);
    }

    /**
     * 批量更新数据
     *
     * @param dataJson 待更新数据json串
     * @return 成功数
     */
    @PostMapping(value = "/batchUpdate")
    default CommonResponse<Object> batchUpdate(String dataJson, D entity) {
        List<D> data = (List<D>) JSON.parseArray(dataJson, entity.getClass());

        // 数据校验
        for (D e : data) {
            this.checkD(e);
        }

        return this.getCommonService().batchUpdate(data);
    }

    /**
     * 删除数据 - 逻辑删除
     *
     * @param ids 待删除ids数组
     * @return 成功数
     */
    @PostMapping(value = "/batchDelete")
    default CommonResponse<Object> batchDelete(Long[] ids) {
        if (Util.isNull(ids)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getCommonService().batchDelete(ids);
    }

    /**
     * 查询主表
     *
     * @param id   主表id
     * @param cols 需要字段
     * @return 主表数据
     */
    @GetMapping(value = "/get")
    default CommonResponse<Object> get(Long id, String[] cols) {
        if (Objects.isNull(id)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        // 开始查询
        Object result = this.getCommonService().get(id).getData();
        if (Objects.isNull(result)) {
            return R.success(result);
        }

        return R.success(result, result.getClass(), cols);
    }

    /**
     * 查询(弹框)
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter     查询DTO信息
     * @param filterJson 查询DTO的json字符串
     * @param cols       需要字段
     * @return 主表数据
     */
    @PostMapping(value = "/listByFilter")
    default CommonResponse<List> listByFilter(F filter, String filterJson, String[] cols, PageInfo pageInfo) {
        // 参数校验
        if (Util.isString(filterJson)) {
            filter = (F) JSON.parseObject(filterJson, filter.getClass());
        }

        // 筛选校验
        cols = this.checkF(filter, cols);

        // 为filter添加pageInfo
        try {
            Map<String, Field> fieldMap = ReflectionUtil.listAllField(filter.getClass());
            Field pageIndexField = fieldMap.get("pageIndex");
            Field pageSizeField = fieldMap.get("pageSize");
            if (Objects.nonNull(pageIndexField) && Objects.nonNull(pageSizeField)) {
                pageIndexField.setAccessible(Boolean.TRUE);
                pageSizeField.setAccessible(Boolean.TRUE);

                Object oldPageIndex = pageIndexField.get(filter);
                Object oldPageSize = pageSizeField.get(filter);
                if (Objects.isNull(oldPageIndex) || Objects.isNull(oldPageSize)) {
                    pageIndexField.set(filter, pageInfo.getPageIndex());
                    pageSizeField.set(filter, pageInfo.getPageSize());
                }

                pageIndexField.setAccessible(Boolean.FALSE);
                pageSizeField.setAccessible(Boolean.FALSE);
            }

        } catch (Exception e) {
            LogUtils.logError("给Filter添加分页信息失败：e", e);
        }

        // 返回数据
        List result = (List) this.getCommonService().listByFilter(filter).getData();

        return R.success(result, cols);
    }

    /**
     * 查询统计(弹框)
     * 描述：与listByFilter配套使用，可以通过withDeleted来统计逻辑删除的数据，默认不统计删除的
     *
     * @param filter     查询DTO信息
     * @param filterJson 查询DTO的json字符串
     * @return 主表数据全部数量
     */
    @PostMapping(value = "/countByFilter")
    default CommonResponse<Integer> countByFilter(F filter, String filterJson) {
        // 参数校验
        if (Util.isString(filterJson)) {
            filter = (F) JSON.parseObject(filterJson, filter.getClass());
        }

        return this.getCommonService().countByFilter(filter);
    }

    /**
     * 主页面查询
     * 描述：不会获取删除的数据
     *
     * @param filter     查询DTO信息
     * @param filterJson 查询DTO的json字符串
     * @param pageInfo   分页信息
     * @return 主页面数据
     */
    @PostMapping(value = "/list")
    default CommonResponse<List> list(F filter, String filterJson, PageInfo pageInfo, String[] cols) {
        if (Util.isString(filterJson)) {
            filter = (F) JSON.parseObject(filterJson, filter.getClass());
        }

        // 筛选校验
        cols = this.checkF(filter, cols);

        // 返回数据
        List result = (List) this.getCommonService().listMain(filter, pageInfo).getData();

        return R.success(result, cols);

    }

    /**
     * 主页面查询统计
     *
     * @param filter     查询DTO信息
     * @param filterJson 查询DTO的json字符串
     * @return 主页面数据数量
     */
    @PostMapping(value = "/list/count")
    default CommonResponse<Integer> listCount(F filter, String filterJson) {
        if (Util.isString(filterJson)) {
            filter = (F) JSON.parseObject(filterJson, filter.getClass());
        }

        return this.getCommonService().listCount(filter);
    }

    /**
     * 统计字段的数量
     * 描述：不会统计删除的数据
     *
     * @param field 对应归类字段
     * @return 字段的数量
     */
    @GetMapping(value = "/listFieldCount")
    default CommonResponse<List> listFieldCount(String field) {
        if (Util.isNotString(field)) {
            throw new ServiceException(CommonResponseCodesEnum.WARN_PARAM_IS_EMPTY);
        }

        return this.getCommonService().listFieldCount(field);
    }
}
