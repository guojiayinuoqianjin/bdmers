package com.bdmer.framework.base.cmomon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entity字段属性信息
 *
 * @author GongDeLang
 * @since 2019/12/10 18:10
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EntityFieldInfo {
    /**
     * 描述（中文名）
     */
    String desc();
}
