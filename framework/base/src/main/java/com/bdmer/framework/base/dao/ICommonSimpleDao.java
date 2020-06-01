package com.bdmer.framework.base.dao;

import com.bdmer.framework.base.dto.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公共DAO - 样例
 * 包含增删改查8种基本函数
 * F 筛选模型
 * D 实体模型
 * R 返回模型
 *
 * @author GongDeLang
 * @since 2019/12/11 13:13
 */
public interface ICommonSimpleDao<F, D, R> extends ICommonDao<F, D, R> {

    /**
     * 批量增加
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    @Override
    Integer batchAdd(@Param("entityList") List<D> entityList);

    /**
     * 批量更新
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    @Override
    Integer batchUpdate(@Param("entityList") List<D> entityList);

    /**
     * 批量逻辑删除
     *
     * @param ids 实体Id数组
     * @return 成功数
     */
    @Override
    Integer batchLogicDelete(@Param("ids") Long[] ids);

    /**
     * 批量删除
     *
     * @param ids 实体Id数组
     * @return 成功数
     */
    @Override
    Integer batchDelete(@Param("ids") Long[] ids);

    /**
     * 批量获取实体
     *
     * @param ids 实体Id数组
     * @return 实体List
     */
    @Override
    List<D> list(@Param("ids") Long[] ids);

    /**
     * 批量根据条件获取实体
     *
     * @param filter 过滤条件
     * @return 实体List
     */
    @Override
    List<D> listByFilter(@Param("filter") F filter);

    /**
     * 批量根据条件统计
     *
     * @param filter 过滤条件
     * @return 数量
     */
    @Override
    Integer countByFilter(@Param("filter") F filter);

    /**
     * 获取主页面信息
     *
     * @param filter   过滤条件
     * @param pageInfo 分页信息
     * @return 实体List
     */
    @Override
    List<R> listMain(@Param("filter") F filter, @Param("pageInfo") PageInfo pageInfo);

    /**
     * 查询主页面数量
     *
     * @param filter 过滤条件
     * @return 数量
     */
    @Override
    Integer listCount(@Param("filter") F filter);

    /**
     * 获取最大的no
     *
     * @return 最大no
     */
    @Override
    Integer getMaxNo();

    /**
     * 统计字段的数量
     *
     * @return 字段的数量
     */
    @Override
    List<Map<String, Object>> listFieldCount(@Param("field") String field);

}



