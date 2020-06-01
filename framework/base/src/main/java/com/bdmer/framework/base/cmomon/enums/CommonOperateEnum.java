package com.bdmer.framework.base.cmomon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基本操作枚举
 *
 * @author GongDeLang
 * @since 2019/12/19 15:49
 */
@Getter
@AllArgsConstructor
public enum CommonOperateEnum {
    /**
     * 添加操作
     */
    ADD,
    /**
     * 更新操作
     */
    UPDATE,
    /**
     * 删除操作
     */
    DELETE;
}
