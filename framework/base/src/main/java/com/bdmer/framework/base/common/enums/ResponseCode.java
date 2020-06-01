package com.bdmer.framework.base.common.enums;

/**
 * 返回码
 *
 * @author GongDeLang
 * @since 2020/6/1 14:57
 */
public interface ResponseCode {
    /**
     * 获取响应码的键
     *
     * @return
     */
    String getSubCode();

    /**
     * 获取响应码的值
     *
     * @return
     */
    String getMessage();
}
