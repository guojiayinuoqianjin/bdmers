package com.bdmer.framework.base.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 反射工具类
 *
 * @author GongDeLang
 * @since 2019/12/6 10:03
 */
@Slf4j
public class ReflectionUtil {

    /**
     * 获取字段的反射
     *
     * @param clazz     类类型
     * @param fieldName 字段名称
     * @param <T>       类泛型
     * @return 反射字段
     */
    public static <T> Field getFiledByName(Class<T> clazz, String fieldName) {
        if (!Util.isString(fieldName)) {
            return null;
        }

        // 获取继承链路上所有字段
        Map<String, Field> fieldMap;
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clazz;
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            // 得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        fieldMap = fieldList.stream().collect(Collectors.toMap(Field::getName, e -> e, (k1, k2) -> k1));

        return fieldMap.get(fieldName);
    }

    /**
     * 获取所有反射字段
     *
     * @param clazz 类类型
     * @param <T>   类泛型
     * @return 反射字段
     */
    public static <T> Map<String, Field> listAllField(Class<T> clazz) {
        // 获取继承链路上所有字段
        Map<String, Field> fieldMap;
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clazz;
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            // 得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        fieldMap = fieldList.stream().collect(Collectors.toMap(Field::getName, e -> e, (k1, k2) -> k1));

        return fieldMap;
    }
}
