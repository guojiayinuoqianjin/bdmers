package com.bdmer.framework.base.common.enums;


/**
 * 公共错误码
 *
 * @author GongDeLang
 * @since 2020/4/15 17:38
 */
public enum CommonResponseCodesEnum implements ResponseCode {

    // ---- 成功信息 ----
    SUCCESS(ResultLevelEnum.SUCCESS, "0000", "成功"),
    /**
     * 成功信息 - 附带消息
     */
    SUCCESS_WITH_MSG(ResultLevelEnum.SUCCESS, "0001", "{0}"),

    // ---- 警告信息 ----
    WARN(ResultLevelEnum.WARN_LOW_LEVEL, "0000", "{0}"),
    WARN_ID_IS_EMPTY(ResultLevelEnum.WARN_LOW_LEVEL, "0001", "ID为空"),
    WARN_PARAM_IS_EMPTY(ResultLevelEnum.WARN_LOW_LEVEL, "0002", "参数为空"),


    // ---- 错误信息 ----
    ERROR(ResultLevelEnum.ERROR_LOW_LEVEL, "0000", "{0}"),
    ERROR_DAO_METHOD_NO_IMPL(ResultLevelEnum.ERROR_HIGH_LEVEL, "0001", "DAO对应方法没有对应MAPPER"),
    ERROR_PARAM_IS_EMPTY(ResultLevelEnum.ERROR_HIGH_LEVEL, "0002", "参数为空"),
    ERROR_LOG_DATA_IS_EMPTY(ResultLevelEnum.ERROR_LOW_LEVEL, "0003", "待记录日志数据为空"),
    ERROR_FILTER_IS_EMPTY(ResultLevelEnum.WARN_LOW_LEVEL, "0004", "Filter为空"),
    ERROR_ID_IS_EMPTY(ResultLevelEnum.ERROR_LOW_LEVEL, "0005", "ID为空"),
    ERROR_ID_DATA_IS_EMPTY(ResultLevelEnum.ERROR_LOW_LEVEL, "0006", "ID对应数据为空"),
    ERROR_REFLECTION_FIELD_IS_EMPTY(ResultLevelEnum.ERROR_HIGH_LEVEL, "0007", "反射字段为空"),

    // ---- 紧急信息 ----
    SERIOUS(ResultLevelEnum.SERIOUS_LOW_LEVEL, "0000", "{0}"),
    ;

    /**
     * 公共服务代码000
     */
    private final String prefix = "000";

    /**
     * 公共模块代码00
     */
    private final String module = "00";
    /**
     * 错误等级
     */
    private final ResultLevelEnum resultLevelEnum;
    /**
     * 代码
     */
    private final String key;
    /**
     * 消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param resultLevelEnum 错误等级
     * @param key             错误序号
     * @param message         错误消息
     */
    CommonResponseCodesEnum(ResultLevelEnum resultLevelEnum, String key, String message) {
        this.resultLevelEnum = resultLevelEnum;
        this.key = key;
        this.message = message;
    }

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 获取完整的错误码
     *
     * @return 完整的错误码
     */
    @Override
    public String getSubCode() {
        return prefix + module + resultLevelEnum.getLevel() + key;
    }
}
