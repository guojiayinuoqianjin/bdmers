package com.bdmer.server.tbk.dao;

import com.bdmer.framework.base.dto.PageInfo;
import com.bdmer.framework.base.dao.ICommonDao;
import com.bdmer.server.tbk.dto.UserFilterDTO;
import com.bdmer.server.tbk.dto.UserResultDTO;
import com.bdmer.server.tbk.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 用户 - DAO
*
* @author GongDeLang
* @since  2020年06月01日
*/
public interface UserDao extends ICommonDao<UserFilterDTO, UserEntity, UserResultDTO> {

    /**
     * 批量新增
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    @Override
    Integer batchAdd(@Param("entityList") List<UserEntity> entityList);

    /**
     * 批量更新
     *
     * @param entityList 实体数组
     * @return 成功数
     */
    @Override
    Integer batchUpdate(@Param("entityList") List<UserEntity> entityList);

    /**
     * 批量删除
     *
     * @param ids 主键数组
     * @return 成功数
     */
    @Override
    Integer batchDelete(@Param("ids") Long[] ids);

    /**
     * 批量获取
     *
     * @param ids 主键数组
     * @return 实体数组
     */
    @Override
    List<UserEntity> list(@Param("ids") Long[] ids);

    /**
     * 批量根据条件获取实体
     *
     * @param filter     过滤条件
     * @return 实体List
     */
    @Override
    List<UserEntity> listByFilter(@Param("filter") UserFilterDTO filter);

    /**
     * 批量根据条件获取实体
     *
     * @param filter     过滤条件
     * @return 实体List
     */
    @Override
    Integer countByFilter(@Param("filter") UserFilterDTO filter);

    /**
     * 获取主页面信息
     *
     * @param filter     过滤条件
     * @param pageInfo   分页信息
     * @return 实体List
     */
    @Override
    List<UserResultDTO> listMain(@Param("filter") UserFilterDTO filter, @Param("pageInfo") PageInfo pageInfo);

     /**
      * 查询主页面数量
      *
      * @param filter     过滤条件
      * @return 数量
      */
     @Override
     Integer listCount(@Param("filter") UserFilterDTO filter);

     /**
      * 统计字段的数量
      *
      * @return 字段的数量
      */
     @Override
     List<Map<String, Object>> listFieldCount(@Param("field") String field);
}