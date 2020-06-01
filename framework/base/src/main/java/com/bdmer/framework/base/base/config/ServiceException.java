package com.bdmer.framework.base.base.config;


import com.bdmer.framework.base.common.enums.ResponseCode;
import com.bdmer.framework.base.dto.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于controller层统一处理的exception
 *
 * @author GongDeLang
 * @since 2020/6/1 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "ServiceException";

    /**
     * 响应码,枚举
     */
    private ResponseCode responseCode;

    /**
     * 响应码
     */
    private String subCode;

    /**
     * 说明
     */
    private String msg;

    public ServiceException() {
        super(DEFAULT_MESSAGE);
    }

    public ServiceException(ResponseCode responseCode) {
        super(responseCode != null ? responseCode.getMessage() : DEFAULT_MESSAGE);
        this.responseCode = responseCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public ServiceException(ResponseCode responseCode, String message, Throwable cause) {
        this(message, cause);
        this.responseCode = responseCode;
    }

    public ServiceException(ResponseCode responseCode, Throwable cause) {
        super(responseCode != null ? responseCode.getMessage() : DEFAULT_MESSAGE, cause);
        this.responseCode = responseCode;
    }

    public ServiceException(ResponseCode responseCode, String... param) {
        super(responseCode != null ? R.messageFormat(responseCode.getMessage(), param) : DEFAULT_MESSAGE);
        if (responseCode != null) {
            this.subCode = responseCode.getSubCode();
        }
        if (param != null) {
            this.msg = R.messageFormat(responseCode.getMessage(), param);
        }
    }

    /**
     * 处理提示语中的占位符
     *
     * @param responseCode 错误码
     * @param cause        出错原因
     * @param param        参数
     */
    public ServiceException(ResponseCode responseCode, Throwable cause, Object... param) {
        super(responseCode != null ? R.messageFormat(responseCode.getMessage(), param) : DEFAULT_MESSAGE, cause);
        if (responseCode != null) {
            this.subCode = responseCode.getSubCode();
        }
        if (param != null) {
            this.msg = R.messageFormat(responseCode.getMessage(), param);
        }
    }
}