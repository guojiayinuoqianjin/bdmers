package com.bdmer.framework.base.base.config;

import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.common.util.Util;
import com.bdmer.framework.base.dto.CommonResponse;
import com.bdmer.framework.base.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * contorller异常捕捉类
 *
 * @author GongDeLang
 * @since 2019/12/3 10:28
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionConfiguration {

    private static final Integer MAX_ERROR_MSG_LENGTH = 200;

    /**
     * ServiceException 异常捕捉
     *
     * @param e 异常信息
     * @return 返回捕获的服务异常
     */
    @ExceptionHandler(ServiceException.class)
    public CommonResponse<Object> serviceExceptionHandler(ServiceException e, HttpServletRequest request) {
        try {
            log.error("[异常拦截器] ServiceException:", e);
            if (e.getResponseCode() == null && e.getSubCode() == null) {
                return R.exception(CommonResponseCodesEnum.ERROR, e.getMessage());
            }
        } catch (Exception ex) {
            log.error("[异常拦截器] ServiceException:", e);
        }

        return R.exception(e.getMsg());
    }

    /**
     * Exception 异常捕捉
     *
     * @param e 异常信息
     * @return 返回捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Object> exceptionHandler(Exception e, HttpServletRequest request) {
        String errorMsg = "";
        try {
            log.error("[异常拦截器] Exception:", e);
            errorMsg = e.toString();
            if (Util.isString(errorMsg) && errorMsg.length() > MAX_ERROR_MSG_LENGTH) {
                errorMsg = errorMsg.substring(0, MAX_ERROR_MSG_LENGTH) + "...";
            }
        } catch (Exception ex) {
            log.error("[异常拦截器] Exception:", ex);
        }

        return R.exception(CommonResponseCodesEnum.ERROR, errorMsg);
    }


}
