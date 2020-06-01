package com.bdmer.framework.base.dto;

import com.bdmer.framework.base.common.enums.CommonResponseCodesEnum;
import com.bdmer.framework.base.common.enums.ResponseCode;
import com.bdmer.framework.base.common.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 返回JSON结果基本方法
 *
 * @author GongDeLang
 * @since 2020/6/1 15:01
 */
@Slf4j
public class R {

    private R() {
        throw new IllegalStateException("R class");
    }

    /**
     * 成功的code
     */
    private static final int SUCCESS = 200;
    /**
     * 失败的code
     */
    private static final int ERROR = 0;
    /**
     * 抛异常的code
     */
    private static final int EXCEPTION = 500;

    /**
     * 判断CommonResponse是否成功,成功返回true,否则返回false
     *
     * @param response 包装的返回结果
     * @return 是否成功
     */
    public static boolean isSuccess(CommonResponse<?> response) {
        if (response == null) {
            return false;
        }
        return response.getCode() == SUCCESS;
    }

    /**
     * 获取CommonResponse的data
     *
     * @param response 包装的返回结果
     * @param <T>      泛型
     * @return 返回真实的结果
     */
    public static <T> T get(CommonResponse<T> response) {

        if (!isSuccess(response)) {
            return null;
        }

        return response.getData();
    }

    /**
     * 返回错误的CommonResponse
     *
     * @param msg 错误信息
     * @param <T> 泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> error(String msg) {
        return CommonResponse.sendFailure(ERROR, "", msg);
    }

    /**
     * 返回错误的CommonResponse, 使用响应码
     *
     * @param responseCode 错误码枚举
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> error(ResponseCode responseCode) {
        if (responseCode == null) {
            return error("");
        }
        return CommonResponse.sendFailure(ERROR, responseCode.getSubCode(), responseCode.getMessage());
    }

    /**
     * 返回错误的CommonResponse,使用响应码和占位符
     *
     * @param responseCode 错误码枚举
     * @param param        参数类型
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> error(ResponseCode responseCode, Object... param) {
        if (responseCode == null) {
            return error("");
        }
        return CommonResponse.sendFailure(ERROR, responseCode.getSubCode(), messageFormat(responseCode.getMessage(), param));
    }


    /**
     * 返回出异常的CommonResponse
     *
     * @param msg 异常信息
     * @param <T> 泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> exception(String msg) {
        return CommonResponse.sendFailure(EXCEPTION, "", msg);
    }

    /**
     * 返回出异常的CommonResponse, 使用响应码
     *
     * @param responseCode 异常码枚举
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> exception(ResponseCode responseCode) {
        if (responseCode == null) {
            return exception("");
        }
        return CommonResponse.sendFailure(EXCEPTION, responseCode.getSubCode(), responseCode.getMessage());
    }

    /**
     * 返回出异常的CommonResponse,使用响应码和占位符
     *
     * @param responseCode 异常码枚举
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> exception(ResponseCode responseCode, Object... param) {
        if (responseCode == null) {
            return exception("");
        }
        return CommonResponse.sendFailure(EXCEPTION, responseCode.getSubCode(), messageFormat(responseCode.getMessage(), param));
    }

    /**
     * 返回成功的CommonResponse
     *
     * @param t   数据
     * @param <T> 泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(T t) {
        return CommonResponse.sendSuccessData(CommonResponseCodesEnum.SUCCESS.getSubCode(), CommonResponseCodesEnum.SUCCESS.getMessage(), t);
    }

    /**
     * 返回成功的CommonResponse
     *
     * @param msg 消息
     * @param <T> 泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(String msg) {
        return CommonResponse.sendSuccessData(CommonResponseCodesEnum.SUCCESS_WITH_MSG.getSubCode(), msg, null);
    }

    /**
     * 返回成功的CommonResponse
     *
     * @param t   数据
     * @param msg 消息
     * @param <T> 泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(String msg, T t) {
        return CommonResponse.sendSuccessData(CommonResponseCodesEnum.SUCCESS_WITH_MSG.getSubCode(), msg, t);
    }

    /**
     * 返回成功的CommonResponse, 使用响应码
     *
     * @param t            数据
     * @param responseCode 返回码枚举
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(ResponseCode responseCode, T t) {

        if (responseCode != null) {
            return CommonResponse.sendSuccessData(responseCode.getSubCode(), responseCode.getMessage(), t);
        }

        return success(t);
    }

    /**
     * 返回成功的CommonResponse, 使用响应码和占位符
     *
     * @param t            数据
     * @param responseCode 返回码枚举
     * @param param        参数
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(ResponseCode responseCode, T t, Object... param) {

        if (responseCode != null) {
            return CommonResponse.sendSuccessData(responseCode.getSubCode(), messageFormat(responseCode.getMessage(), param), t);
        }

        return success(t);
    }

    /**
     * 返回成功的CommonResponse, 使用响应码
     *
     * @param responseCode 返回码枚举
     * @param <T>          泛型
     * @return 返回包装结果
     */
    public static <T> CommonResponse<T> success(ResponseCode responseCode) {

        if (responseCode != null) {
            return CommonResponse.sendSuccessData(responseCode.getSubCode(), responseCode.getMessage(), null);
        }

        return success("");
    }

    /**
     * 返回成功 - 附带成功码 - 按需返回字段
     *
     * @param t    返回数据
     * @param cols 需要哪些字段
     * @return 返回包装数据
     */
    @SuppressWarnings("unckecked")
    public static <T> CommonResponse<Object> success(List<T> t, String[] cols) {
        if (Util.isNull(cols) || Util.isNull(t)) {
            return R.success(CommonResponseCodesEnum.SUCCESS, t);
        }

        try {
            // 获取继承链路上所有字段
            List<Field> fieldList = new ArrayList<>();
            Class tempClass = t.get(0).getClass();
            // 当父类为null的时候说明到达了最上层的父类(Object类)
            while (tempClass != null) {
                fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                // 得到父类,然后赋给自己
                tempClass = tempClass.getSuperclass();
            }

            List<String> colList = Arrays.stream(cols).collect(Collectors.toList());
            // 按需返回字段

            List<Map> response = new ArrayList<>();
            for (Object o : t) {
                // 字段数量一般再20左右
                Map<String, Object> responseMap = new HashMap<>(32);

                for (Field field : fieldList) {
                    field.setAccessible(true);
                    if (colList.contains(field.getName())) {
                        responseMap.put(field.getName(), field.get(o));
                    }
                    field.setAccessible(false);
                }
                response.add(responseMap);
            }

            return R.success(CommonResponseCodesEnum.SUCCESS, response);
        } catch (Exception e) {
            log.error("[R - success] 按需返回字段 e:{}", e.getMessage());
        }

        return R.success(CommonResponseCodesEnum.SUCCESS, t);
    }

    /**
     * 返回成功 - 附带成功码 - 按需返回字段
     *
     * @param t    返回数据
     * @param <T>  返回包装数据泛型
     * @param cols 需要哪些字段
     * @return 返回包装数据
     */
    @SuppressWarnings("unckecked")
    public static <T, D> CommonResponse<Object> success(T t, Class<D> clazz, String[] cols) {
        if (Util.isNull(cols) || Util.isNull(clazz) || Util.isNull(t)) {
            return R.success(CommonResponseCodesEnum.SUCCESS, t);
        }

        // 获取继承链路上所有字段
        List<Field> fieldList = new ArrayList<>();

        Class tempClass = clazz;
        // 当父类为null的时候说明到达了最上层的父类(Object类)
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            // 得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }

        List<String> colList = Arrays.stream(cols).collect(Collectors.toList());
        // 按需返回字段
        try {
            // t属于数组类型
            if (t instanceof List) {
                List<Map> response = new ArrayList<>();
                for (Object o : (List) t) {
                    // 字段数量一般再20左右
                    Map<String, Object> responseMap = new HashMap<>(32);

                    for (Field field : fieldList) {
                        field.setAccessible(true);
                        if (colList.contains(field.getName())) {
                            responseMap.put(field.getName(), field.get(o));
                        }
                        field.setAccessible(false);
                    }
                    response.add(responseMap);
                }

                return R.success(CommonResponseCodesEnum.SUCCESS, response);
            }

            // t数据单对象类型
            // 字段数量一般再20左右
            Map<String, Object> response = new HashMap<>(32);
            for (Field field : fieldList) {
                if (colList.contains(field.getName())) {
                    response.put(field.getName(), field.get(t));
                }
            }

            return R.success(CommonResponseCodesEnum.SUCCESS, response);
        } catch (Exception e) {
            log.error("[R - success] 按需返回字段 e:{}", e.getMessage());
        }

        return R.success(CommonResponseCodesEnum.SUCCESS, t);
    }

    /**
     * 格式化占位符
     *
     * @param message 待格式化字符串
     * @param param   参数
     * @return 返回结果
     */
    public static String messageFormat(String message, Object... param) {

        if (StringUtils.isBlank(message) || param == null || param.length == 0) {
            return message;
        }

        try {
            return MessageFormat.format(message, param);
        } catch (Exception e) {
            return message;
        }
    }
}
