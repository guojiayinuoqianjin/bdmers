package com.bdmer.framework.base.dto;

import lombok.Data;

/**
 * 返回结果
 *
 * @author GongDeLang
 * @since 2020/6/1 14:53
 */
@Data
public class CommonResponse<T> {
    /**
     * 状态码：200成功、0失败、500异常
     */
    private Integer code;
    /**
     * 具体返回码
     */
    private String subCode;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 具体返回结果
     */
    private T data;

    /**
     * 发送成功消息,自定义填充提示信息（带数据）
     *
     * @param data：业务数据
     * @return 返回结果
     */
    public static <T> CommonResponse<T> sendSuccessData(String subCode, String message, T data) {
        CommonResponse<T> result = new CommonResponse<>();
        result.setCode(200);
        result.setSubCode(subCode);
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    /**
     * 发送错误消息,自定义填充提示信息
     *
     * @param code：0失败、500异常
     * @param subCode：错误编码
     * @param error：错误信息
     * @return 返回结果
     */
    public static <T> CommonResponse<T> sendFailure(int code, String subCode, String error) {
        CommonResponse<T> result = new CommonResponse<>();
        result.setCode(code);
        result.setSubCode(subCode);
        result.setMsg(error);
        return result;
    }
}
