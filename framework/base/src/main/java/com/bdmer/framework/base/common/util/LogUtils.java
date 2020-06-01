package com.bdmer.framework.base.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志操作类
 *
 * @author GongDeLang
 * @since 2020/6/1 14:03
 */
@Slf4j
public class LogUtils {
    private LogUtils() {
        throw new IllegalStateException("LogUtils class");
    }

    /**
     * 为接口默认方法中提供日志记录
     *
     * @param msg 消息
     */
    public static void logInfo(String msg) {
        log.info(msg);
    }

    /**
     * 为接口默认方法中提供日志记录
     *
     * @param msg 消息
     */
    public static void logError(String msg) {
        log.error(msg);
    }

    /**
     * 为接口默认方法中提供日志记录
     *
     * @param e 错误
     */
    public static void logError(Exception e) {
        log.error("Exception:", e);
    }

    /**
     * 为接口默认方法中提供日志记录
     *
     * @param msg 消息
     * @param e   错误
     */
    public static void logError(String msg, Exception e) {
        log.error("{}-e{}", msg, e.getMessage());
    }
}
