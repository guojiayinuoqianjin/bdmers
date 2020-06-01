package com.bdmer.server.tbk.entity;

import com.bdmer.framework.base.common.annotation.EntityFieldInfo;
import lombok.Data;

import java.util.Date;


/**
 * 用户 - Entity
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@Data
public class UserEntity {
    /**
     * 标识
     */
    private Long id;
    /**
     * 名称
     */
    @EntityFieldInfo(desc = "名称")
    private String name;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;

}