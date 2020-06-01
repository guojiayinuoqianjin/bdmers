package com.bdmer.framework.base.core;


import com.bdmer.framework.base.base.config.ServiceException;
import com.bdmer.framework.base.cmomon.constant.MysqlConstant;
import com.bdmer.framework.base.cmomon.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.cmomon.util.LogUtils;
import com.bdmer.framework.base.cmomon.util.Util;
import com.bdmer.framework.base.dao.ICommonDao;
import com.bdmer.framework.base.dto.PageInfo;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 公共core层方法
 * F 筛选数据模型
 * D 实体数据模型
 * R 返回数据模型
 *
 * @author GongDeLang
 * @since 2019/12/12 9:52
 */
@SuppressWarnings("unchecked")
public interface ICommonCore<F, D, R> {
    /**
     * 获取对应的Dao
     *
     * @return dao实例
     */
    ICommonDao getCommonDao();

    /**
     * 检查过滤条件
     * 注意：不仅仅对Filter作NPE过滤，也可以通过业务代码实现关联查询，因此若有关联查询，建议重写该接口
     *
     * @param filter 过滤条件
     * @return 过滤条件
     */
    default F checkFilter(F filter) {
        return filter;
    }

    /**
     * 添加操作 - 批量数据校验
     *
     * @param entityList 实体List
     */
    default void checkDataForAdd(List<D> entityList) {
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }
    }

    /**
     * 修改操作 - 批量数据校验
     *
     * @param entityList 实体List
     */
    default void checkDataForUpdate(List<D> entityList) {
        if (Util.isNull(entityList)) {
            throw new ServiceException(CommonResponseCodesEnum.ERROR_PARAM_IS_EMPTY);
        }
    }

    /**
     * 添加
     *
     * @param entity 实体
     * @return 成功数
     */
    default Integer add(D entity) {
        // 数据构建
        List<D> entityList = new ArrayList<>();
        entityList.add(entity);
        return this.batchAdd(entityList);
    }

    /**
     * 批量添加
     *
     * @param entityList 实体List
     * @return 成功数
     */
    default Integer batchAdd(List<D> entityList) {
        // 数据校验
        this.checkDataForAdd(entityList);

        // 分批次添加
        Integer successCount = 0;
        List<List<D>> tempListList = ListUtils.partition(entityList, MysqlConstant.MAX_INSERT_NUM);
        for (List<D> tempList : tempListList) {
            // 不是第一次插入需要延时10毫秒
            if (successCount > 0) {
                try {
                    Thread.sleep(MysqlConstant.MAX_SLEEP_TIME);
                } catch (Exception e) {
                    LogUtils.logError("批量添加数据延时", e);
                }
            }

            successCount += this.getCommonDao().batchAdd(tempList);
        }

        return successCount;
    }

    /**
     * 更新
     *
     * @param entity 实体
     * @return 成功数
     */
    default Integer update(D entity) {
        // 数据构建
        List<D> entityList = new ArrayList<>();
        entityList.add(entity);
        return this.batchUpdate(entityList);
    }

    /**
     * 批量更新
     *
     * @param entityList 实体List
     * @return 成功数
     */
    default Integer batchUpdate(List<D> entityList) {
        // 数据校验
        this.checkDataForUpdate(entityList);

        // 分批次更新
        Integer successCount = 0;
        List<List<D>> tempListList = ListUtils.partition(entityList, MysqlConstant.MAX_UPDATE_NUM);
        for (List<D> tempList : tempListList) {
            // 不是第一次插入需要延时10毫秒
            if (successCount > 0) {
                try {
                    Thread.sleep(MysqlConstant.MAX_SLEEP_TIME);
                } catch (Exception e) {
                    LogUtils.logError("批量更新数据延时", e);
                }
            }

            successCount += this.getCommonDao().batchUpdate(tempList);
        }

        return successCount;
    }

    /**
     * 逻辑删除
     *
     * @param id Id
     * @return 成功数
     */
    default Integer logicDelete(Long id) {
        // 数据校验
        if (Objects.isNull(id)) {
            return 0;
        }

        return this.batchLogicDelete(new Long[]{id});
    }

    /**
     * 批量逻辑删除
     *
     * @param ids Id数组
     * @return 成功数
     */
    default Integer batchLogicDelete(Long[] ids) {
        // 数据校验
        if (Util.isNull(ids)) {
            return 0;
        }

        return this.getCommonDao().batchLogicDelete(ids);
    }

    /**
     * 删除
     *
     * @param id Id
     * @return 成功数
     */
    default Integer delete(Long id) {
        // 数据校验
        if (Objects.isNull(id)) {
            return 0;
        }

        return this.batchDelete(new Long[]{id});
    }

    /**
     * 批量删除
     *
     * @param ids Id数组
     * @return 成功数
     */
    default Integer batchDelete(Long[] ids) {
        // 数据校验
        if (Util.isNull(ids)) {
            return 0;
        }

        return this.getCommonDao().batchDelete(ids);
    }

    /**
     * 获取实体 - 唯一可能会返回null的接口，调用者需要注意
     *
     * @param id 实体Id
     * @return 实体
     */
    default D get(Long id) {
        // 数据校验
        if (Objects.isNull(id)) {
            return null;
        }

        List<D> result = this.list(new Long[]{id});
        // 如果数据为null或者长度等于0
        if (Util.isNull(result)) {
            return null;
        }

        return result.get(0);
    }

    /**
     * 批量获取实体
     *
     * @param ids 实体Id数组
     * @return 实体List
     */
    default List<D> list(Long[] ids) {
        // 数据校验
        if (Util.isNull(ids)) {
            return new ArrayList<>();
        }

        return this.getCommonDao().list(ids);
    }

    /**
     * 根据条件查询
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter 过滤条件
     * @return 实体List
     */
    default List<D> listByFilter(F filter) {
        filter = this.checkFilter(filter);

        return this.getCommonDao().listByFilter(filter);
    }

    /**
     * 根据条件统计
     * 描述：可以根据cols按需获取字段，也可以通过withDeleted来获取逻辑删除的数据，默认不查询删除的
     *
     * @param filter 过滤条件
     * @return 数量
     */
    default Integer countByFilter(F filter) {
        filter = this.checkFilter(filter);

        return this.getCommonDao().countByFilter(filter);
    }

    /**
     * 获取主页面信息
     * 描述：不会获取删除的数据
     *
     * @param filter   过滤条件
     * @param pageInfo 分页信息
     * @return 主页面结果List
     */
    default List<R> listMain(F filter, PageInfo pageInfo) {
        if (Util.isNull(pageInfo)) {
            pageInfo = new PageInfo();
        }

        filter = this.checkFilter(filter);

        return this.getCommonDao().listMain(filter, pageInfo);
    }

    /**
     * 根据条件查询
     *
     * @param filter 过滤条件
     * @return 主页面结果数量
     */
    default Integer listCount(F filter) {
        filter = this.checkFilter(filter);

        return this.getCommonDao().listCount(filter);
    }

    /**
     * 统计字段的数量
     * 描述：不会统计删除的数据
     *
     * @param field 对应归类字段
     * @return 字段的数量
     */
    default List listFieldCount(String field) {
        return this.getCommonDao().listFieldCount(field);
    }
}
