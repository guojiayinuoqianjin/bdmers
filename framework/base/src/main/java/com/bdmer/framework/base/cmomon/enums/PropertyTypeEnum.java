package com.bdmer.framework.base.cmomon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性类型枚举
 *
 * @author GongDeLang
 * @since 2020/6/1 14:23
 */
@Getter
@AllArgsConstructor
public enum PropertyTypeEnum {
    /**
     * 数据类型
     */
    BYTE("java.lang.Byte"),

    INTEGER("java.lang.Integer"),

    LONG("java.lang.Long"),

    DOUBLE("java.lang.Double"),

    FLOAT("java.lang.Float"),

    STRING("java.lang.String"),

    BIG_DECIMAL("java.math.BigDecimal"),

    DATE("java.util.Date"),;

    /**
     * 类型名称
     */
    private final String propertyTypeName;

    /**
     * 根据属性类型名获取枚举
     *
     * @param propertyTypeName
     *            属性类型名
     * @return 属性枚举
     */
    public static PropertyTypeEnum getEnumByName(String propertyTypeName) {
        PropertyTypeEnum propertyTypeEnum = null;
        if (propertyTypeName == null) {
            return propertyTypeEnum;
        }
        for (PropertyTypeEnum proper : PropertyTypeEnum.values()) {
            if (!proper.getPropertyTypeName().equals(propertyTypeName)) {
                continue;
            }
            propertyTypeEnum = proper;
            break;
        }

        return propertyTypeEnum;
    }
}
