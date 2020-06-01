package com.bdmer.framework.base.dao;

import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.dto.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公共DAO
 * 包含增删改查10种基本函数
 * F 筛选模型
 * D 实体模型
 * R 返回模型
 *
 * @author GongDeLang
 * @since 2019/12/11 13:13
 */
public interface ICommonDao<F, D, R> {

    /**
     * 批量增加
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    default Integer batchAdd(@Param("entityList") List<D> entityList) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量更新
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    default Integer batchUpdate(@Param("entityList") List<D> entityList) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量逻辑删除 - 表结构必须有is_delete字段才需要实现
     *
     * @param ids 实体Id数组
     * @return 成功数
     */
    default Integer batchLogicDelete(@Param("ids") Long[] ids) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量删除
     *
     * @param ids 实体Id数组
     * @return 成功数
     */
    default Integer batchDelete(@Param("ids") Long[] ids) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量获取实体
     *
     * @param ids        实体Ids
     * @return 实体List
     */
    default List<D> list(@Param("ids") Long[] ids) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量根据条件获取实体
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter     过滤条件
     * @return 实体List
     */
    default List<D> listByFilter(@Param("filter") F filter) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 批量根据条件统计数量
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter 过滤条件
     * @return 数量
     */
    default Integer countByFilter(@Param("filter") F filter) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 获取主页面信息
     * 描述：不会获取删除的数据
     *
     * @param filter     过滤条件
     * @param pageInfo   分页信息
     * @return 实体List
     */
    default List<R> listMain(@Param("filter") F filter, @Param("pageInfo") PageInfo pageInfo) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 查询主页面数量
     *
     * @param filter     过滤条件
     * @return 数量
     */
    default Integer listCount(@Param("filter") F filter) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 获取最大的no - 表结构必须有no字段才需要实现
     *
     * @return 最大no
     */
    default Integer getMaxNo() {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }

    /**
     * 统计字段的数量
     * 描述：不会统计删除的数据
     *
     * @return 字段的数量
     */
    default List<Map<String, Object>> listFieldCount(@Param("field") String field) {
        throw new ServiceException(CommonResponseCodesEnum.ERROR_DAO_METHOD_NO_IMPL);
    }
}
