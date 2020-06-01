package com.bdmer.framework.base.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误等级枚举
 *
 * @author GongDeLang
 * @since 2019/12/4 12:16
 */
@Getter
@AllArgsConstructor
public enum ResultLevelEnum {
    /**
     * 错误等级定义
     **/
    SUCCESS(0, "成功"),
    WARN_LOW_LEVEL(1, "普通警告"),
    WARN_MEDIUM_LEVEL(2, "一般警告"),
    WARN_HIGH_LEVEL(3, "严重警告"),
    ERROR_LOW_LEVEL(4, "普通错误"),
    ERROR_MEDIUM_LEVEL(5, "一般错误"),
    ERROR_HIGH_LEVEL(6, "严重错误"),
    SERIOUS_LOW_LEVEL(7, "普通紧急"),
    SERIOUS_MEDIUM_LEVEL(8, "一般紧急"),
    SERIOUS_HIGH_LEVEL(9, "严重紧急");

    /**
     * 错误等级
     */
    private final Integer level;
    /**
     * 错误等级描述
     */
    private final String levelName;
}
