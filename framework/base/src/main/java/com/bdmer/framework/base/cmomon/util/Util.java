package com.bdmer.framework.base.cmomon.util;

import com.alibaba.fastjson.JSON;
import com.bdmer.framework.base.cmomon.annotation.EntityFieldInfo;
import com.bdmer.framework.base.cmomon.constant.BaseConstant;
import com.bdmer.framework.base.cmomon.enums.PropertyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 基本工具类
 *
 * @author GongDeLang
 * @since 2020/2/28 14:25
 */
@Slf4j
public class Util {

    private Util() {
        throw new IllegalStateException("Util class");
    }

    /**
     * 数字正则表达式
     */
    private static final String IS_NUMBER_PATTERN = "^[-\\+]?[\\d]*$";

    /**
     * 下划线匹配
     */
    public static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    /**
     * 驼峰匹配
     */
    public static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 大数减法偏差值
     **/
    private static final double BIGDECIMAL_BIAS = 1e-5;

    /**
     * 字符串转LIST的分割符号
     */
    private static final String STR_TO_LIST_SPLIT_SYMBOL = ",";

    /**
     * 判断字符串是否为空
     *
     * @param message 需检测的字符串
     * @return 是否为字符串
     */
    public static boolean isString(String message) {
        return null != message && !"".equals(message.trim());
    }

    /**
     * 判断字符串是否为空
     *
     * @param message 需检测的字符串
     * @return 是否为字符串
     */
    public static boolean isNotString(String message) {
        return !Util.isString(message);
    }

    /**
     * 字符串为空返回""
     *
     * @param message 需检测的字符串
     * @return 字符串
     */
    public static String getString(String message) {
        return Util.isString(message) ? message : "";
    }

    /**
     * 判断字符串是否数字
     *
     * @param message 需检测的字符串
     * @return 是否为数字
     */
    public static boolean isNumber(String message) {
        if (!isString(message)) {
            return false;
        }
        Pattern pattern = Pattern.compile(IS_NUMBER_PATTERN);
        return pattern.matcher(message).matches();
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj 对象
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (Object o : object) {
                if (!isNull(o)) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 判断obj不是空
     *
     * @param obj 对象
     * @return 不为空
     */
    public static boolean nonNull(Object obj) {
        return !Util.isNull(obj);
    }

    /**
     * 判断对象中的每一个属性是否都为null
     *
     * @param object 对象
     * @return true-是全为null,false-不全为null
     */
    public static Boolean isPropNull(Object object) {
        if (null == object) {
            return true;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return false;
                }

            }
        } catch (Exception e) {
            log.error("判断对象中的每一个属性是否都为null 错误 e", e);
        }

        return true;
    }

    /**
     * 字符串转List - 通过','
     *
     * @param str 字符串
     * @return 字符串List - 调用者根据需要来转化成自己的类型(Long、Integer)
     */
    public static List<String> strToList(String str) {
        if (Util.isNotString(str)) {
            return new ArrayList<>();
        }

        return Arrays.stream(str.split(STR_TO_LIST_SPLIT_SYMBOL))
                .filter(Util::isString)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 字符串转Array
     *
     * @param str 字符串
     * @return 字符串Array - 调用者根据需要来转化成自己的类型(Long、Integer)
     */
    public static String[] strToArray(String str) {
        if (Util.isNotString(str)) {
            return new String[]{};
        }

        return Arrays.stream(str.split(STR_TO_LIST_SPLIT_SYMBOL))
                .filter(Util::isString)
                .distinct()
                .toArray(String[]::new);
    }

    /**
     * 将Long[]数组以,分组拼成字符串
     *
     * @param nums Long数组
     * @return 字符串 - 逗号隔开
     */
    public static <T> String arrLongToString(T[] nums) {
        if (Util.isNull(nums)) {
            return null;
        }

        return StringUtils.join(nums, ",");
    }

    /**
     * 将List<Long>转为数组Long[]
     *
     * @param list Long - List
     * @return Long 数组
     */
    public static Long[] listToArray(List<Long> list) {
        if (Util.isNull(list)) {
            return new Long[0];
        }
        Long[] arr = new Long[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * 截取字符串,中文算2个字符
     *
     * @param str    字符串
     * @param length 长度
     * @return 字符串
     */
    public static String subString(String str, Integer length) {
        if (str == null) {
            return null;
        }

        if (str.length() < length / 2) {
            return str;
        }
        try {
            int count = 0;
            StringBuilder sb = new StringBuilder();
            String[] stringArr = str.split("");
            for (String s : stringArr) {
                count += s.getBytes("GBK").length > 1 ? 2 : 1;
                if (count > length) {
                    break;
                }
                sb.append(s);
            }

            return (sb.toString().length() < str.length()) ? sb.append("...").toString() : str;

        } catch (Exception ex) {
            log.info("字符串截取失败：e", ex);
            return str;
        }
    }

    /**
     * BigDecimal的加法运算封装
     *
     * @param b1 数字1
     * @param bn 数字n
     * @return 返回结果
     */
    public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }

        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.add(null == b ? BigDecimal.ZERO : b);
            }
        }

        return b1;

    }

    /**
     * 判断num1是否小于num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1小于num2返回true
     */
    public static boolean lessThan(BigDecimal num1, BigDecimal num2) {
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }

        return num1.compareTo(num2) < 0;
    }

    /**
     * 判断num1是否小于等于num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1小于或者等于num2返回true
     */
    public static boolean lessEqual(BigDecimal num1, BigDecimal num2) {
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }

        return (num1.compareTo(num2) < 0) || (num1.compareTo(num2) == 0);
    }

    /**
     * 判断num1是否大于num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1大于num2返回true
     */
    public static boolean greaterThan(BigDecimal num1, BigDecimal num2) {
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }

        return num1.compareTo(num2) > 0;
    }

    /**
     * 判断num1是否大于等于num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1大于或者等于num2返回true
     */
    public static boolean greaterEqual(BigDecimal num1, BigDecimal num2) {
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }

        return (num1.compareTo(num2) > 0) || (num1.compareTo(num2) == 0);
    }

    /**
     * 判断num1是否等于num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1等于num2返回true
     */
    public static boolean equalBigDecimal(BigDecimal num1, BigDecimal num2) {
        if (Objects.equals(num1, num2)) {
            return true;
        }
        if (Objects.isNull(num1) || Objects.isNull(num2)) {
            return false;
        }
        return num1.compareTo(num2) == 0;
    }

    /**
     * 大数浮点数比较，a等于b return true
     *
     * @param a 数字1
     * @param b 数字2
     * @return 返回结果
     */
    public static boolean judgeBigDeciEqual(BigDecimal a, BigDecimal b) {
        if (Objects.isNull(a) || Objects.isNull(b)) {
            return false;
        }

        return a.subtract(b).abs().compareTo(BigDecimal.valueOf(BIGDECIMAL_BIAS)) < 0;
    }

    /**
     * BigDecimal的除法运算封装，如果除数或者被除数为0，返回默认值 默认返回小数位后6位，用于金额计算
     *
     * @param b1           数字1
     * @param b2           数字2
     * @param defaultValue 默认值
     * @return 返回结果
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
        if (null == b1 || null == b2) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 6, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * BigDecimal的乘法运算封装
     *
     * @param b1 数字1
     * @param b2 数字2
     * @return 相乘结果
     */
    public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * BigDecimal求百分比的运算,返回格式:number%
     *
     * @param number1 除数
     * @param number2 被除数
     * @return 返回百分数, 保留两位小数
     */
    public static String bigPercentage(BigDecimal number1, BigDecimal number2) {
        String result = "";
        if (null == number1 || null == number2) {
            return result;
        }
        BigDecimal diviceResult = number1.divide(number2, 4, RoundingMode.HALF_UP);
        result = diviceResult.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP) + "%";
        return result;
    }

    /**
     * 获取list中存放的最后一个元素
     *
     * @param list list
     * @param <T>  对象泛型
     * @return 最后一个元素
     */
    public static <T> T getLastElement(List<T> list) {
        if (Util.isNull(list)) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    /**
     * 获取调用的类名
     *
     * @return String
     */
    public static String getClassName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getClassName();
    }

    /**
     * 获取调用的方法名
     *
     * @return String
     */
    public static String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getMethodName();
    }

    /**
     * 获取调用文件名
     *
     * @return 文件数
     */
    public static String getFileName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getFileName();
    }

    /**
     * 获取调用行数
     *
     * @return 行数
     */
    public static int getLineNumber() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        return e.getLineNumber();
    }

    /**
     * 校验属性
     *
     * @param target 实体
     * @param <T>    实体类型
     * @return 实体
     */
    public static <T> T checkProperty(T target) {
        if (target == null) {
            return target;
        }
        try {

            List<Field> fieldList = new ArrayList<>();

            Class<?> clazz = target.getClass();
            // 获取所有属性
            while (clazz != null) {
                fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
                //得到父类,然后赋给自己
                clazz = clazz.getSuperclass();
            }

            Field[] fields = fieldList.toArray(new Field[0]);
            if (fields.length == 0) {
                return target;
            }

            // 遍历所有的属性
            for (Field field : fields) {
                // 得到属性
                // 打开私有访问
                field.setAccessible(true);
                // 获取属性值
                Object value = field.get(target);
                // 获取属性类型
                String type = field.getType().getTypeName();
                PropertyTypeEnum propertyTypeEnum = PropertyTypeEnum.getEnumByName(type);
                if (propertyTypeEnum == null) {
                    continue;
                }
                switch (propertyTypeEnum) {
                    case BYTE:
                        value = value == null ? Byte.valueOf((byte) 0) : value;
                        break;
                    case INTEGER:
                        value = value == null ? 0 : value;
                        break;
                    case LONG:
                        value = value == null ? 0L : value;
                        break;
                    case FLOAT:
                        value = value == null ? Float.valueOf(0) : value;
                        break;
                    case DOUBLE:
                        value = value == null ? Double.valueOf(0) : value;
                        break;
                    case STRING:
                        value = value == null ? "" : value;
                        break;
                    case BIG_DECIMAL:
                        value = value == null ? BigDecimal.ZERO : value;
                        break;
                    default:
                        break;
                }
                // 重新设置属性值
                field.set(target, value);
            }
        } catch (Exception e) {
            log.info("【实体属性校验失败】target:{},异常信息:{}", JSON.toJSONString(target), JSON.toJSONString(e));
        }

        return target;
    }

    /**
     * 批量属性设置默认值
     *
     * @param targetList 实体List
     * @param <T>        实体类型
     */
    public static <T> void checkProperty(List<T> targetList) {
        if (Util.isNull(targetList)) {
            return;
        }

        // 开始设置默认值
        try {

            List<Field> fieldList = new ArrayList<>();
            Class<?> clazz = targetList.get(0).getClass();

            // 获取所有属性
            while (clazz != null) {
                fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
                //得到父类,然后赋给自己
                clazz = clazz.getSuperclass();
            }

            // 遍历所有的属性
            for (Field field : fieldList) {
                // 打开私有访问
                field.setAccessible(true);
                // 获取属性类型
                String type = field.getType().getTypeName();
                PropertyTypeEnum propertyTypeEnum = PropertyTypeEnum.getEnumByName(type);
                if (Objects.isNull(propertyTypeEnum)) {
                    continue;
                }

                // 给所有对象设置默认值
                for (T target : targetList) {
                    // 获取属性值
                    Object value = field.get(target);

                    // 重新设置属性值
                    switch (propertyTypeEnum) {
                        case BYTE:
                            value = Objects.isNull(value) ? Byte.valueOf((byte) 0) : value;
                            break;
                        case INTEGER:
                            value = Objects.isNull(value) ? 0 : value;
                            break;
                        case LONG:
                            value = Objects.isNull(value) ? 0L : value;
                            break;
                        case FLOAT:
                            value = Objects.isNull(value) ? Float.valueOf(0) : value;
                            break;
                        case DOUBLE:
                            value = Objects.isNull(value) ? Double.valueOf(0) : value;
                            break;
                        case STRING:
                            value = Objects.isNull(value) ? "" : value;
                            break;
                        case BIG_DECIMAL:
                            value = Objects.isNull(value) ? BigDecimal.ZERO : value;
                            break;
                        default:
                            break;
                    }

                    // 重新设置属性值
                    field.set(target, value);
                }

                // 关闭私有访问
                field.setAccessible(false);
            }
        } catch (Exception e) {
            log.info("【批量设置实体属性默认值】targetList:{},异常信息:{}", JSON.toJSONString(targetList), JSON.toJSONString(e));
        }
    }
    
    /**
     * ListEntity转List - eg: idList
     *
     * @param entityList list数据
     * @param fieldName  字段名称
     * @param <V>        value类型
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static <V, E> List<V> listEntityToList(List<E> entityList, String fieldName) {
        // 返回值准备
        List<V> result = new ArrayList<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), fieldName);
        if (Objects.isNull(field)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field.setAccessible(Boolean.TRUE);

        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转List失败, ex", ex);
                    return false;
                }
            }).map(e -> {
                V value = null;
                try {
                    value = (V) field.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转List失败, ex", ex);
                }
                return value;
            }).distinct().collect(Collectors.toList());

        } catch (Exception e) {
            LogUtils.logError("Util List转List失败, e", e);
        }

        field.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * ListEntity转Long - eg: ids
     *
     * @param entityList list数据
     * @param fieldName  字段名称
     * @param <V>        value类型
     * @return Long - eg: idList
     */
    @SuppressWarnings("unchecked")
    public static <V, E> V[] listEntityToArray(List<E> entityList, String fieldName) {
        List<V> resultList = Util.listEntityToList(entityList, fieldName);

        if (Util.isNull(resultList)) {
            return null;
        }

        // 使用(V[])list.toArray()导致不能强转失败的问题
        return resultList.toArray((V[]) Array.newInstance(resultList.get(0).getClass(), resultList.size()));
    }

    /**
     * List转Map
     *
     * @param entityList list数据
     * @param key        主键
     * @param <K>        key类型
     * @param <V>        value类型
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> listToMap(List<V> entityList, String key) {
        // 返回值准备
        Map<K, V> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key);
        if (Objects.isNull(field)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field.setAccessible(Boolean.TRUE);

        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转Map失败, ex", ex);
                    return false;
                }
            }).collect(Collectors.toMap(e -> {
                K keyV = null;
                try {
                    keyV = (K) field.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转Map失败, ex", ex);
                }
                return keyV;
            }, e -> e, (k1, k2) -> k1));

        } catch (Exception e) {
            LogUtils.logError("Util List转Map失败, e", e);
        }

        field.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List转MapMap
     *
     * @param entityList list数据
     * @param key1       主键1
     * @param key2       主键2
     * @param <K1>       key1类型
     * @param <K2>       key1类型
     * @param <V>        value类型
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <K1, K2, V> Map<K1, K2> listToMap(List<V> entityList, String key1, String key2) {
        // 返回值准备
        Map<K1, K2> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field1 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key1);
        Field field2 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key2);
        if (Objects.isNull(field1) || Objects.isNull(field2)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field1.setAccessible(Boolean.TRUE);
        field2.setAccessible(Boolean.TRUE);

        String errorMsg = "Util List转Map1失败, ex";
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                    return false;
                }
            }).collect(Collectors.toMap(e -> {
                K1 key1V = null;
                try {
                    key1V = (K1) field1.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key1V;
            }, e -> {
                K2 key2V = null;
                try {
                    key2V = (K2) field2.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key2V;
            }, (k1, k2) -> k1));

        } catch (Exception e) {
            LogUtils.logError(errorMsg, e);
        }

        field1.setAccessible(Boolean.FALSE);
        field2.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List转MapMap
     *
     * @param entityList list数据
     * @param key1       主键1
     * @param key2       主键2
     * @param <K1>       key1类型
     * @param <K2>       key1类型
     * @param <V>        value类型
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <K1, K2, V> Map<K1, Map<K2, V>> listToMapMap(List<V> entityList, String key1, String key2) {
        // 返回值准备
        Map<K1, Map<K2, V>> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field1 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key1);
        Field field2 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key2);
        if (Objects.isNull(field1) || Objects.isNull(field2)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field1.setAccessible(Boolean.TRUE);
        field2.setAccessible(Boolean.TRUE);

        String errorMsg = "Util List转MapMap失败, ex";
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                    return false;
                }
            }).collect(Collectors.groupingBy(e -> {
                K1 key1V = null;
                try {
                    key1V = (K1) field1.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key1V;
            }, Collectors.toMap(e -> {
                K2 key2V = null;
                try {
                    key2V = (K2) field2.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key2V;
            }, e -> e, (k1, k2) -> k1)));

        } catch (Exception e) {
            LogUtils.logError(errorMsg, e);
        }

        field1.setAccessible(Boolean.FALSE);
        field2.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List转GroupMap - 归类Map
     *
     * @param entityList list数据
     * @param key        主键
     * @param <K>        key类型
     * @param <V>        value类型
     * @return GroupMap
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, List<V>> listToGroupMap(List<V> entityList, String key) {
        // 返回值准备
        Map<K, List<V>> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key);
        if (Objects.isNull(field)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field.setAccessible(Boolean.TRUE);

        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转GroupMap失败, ex", ex);
                    return false;
                }
            }).collect(Collectors.groupingBy(e -> {
                K keyV = null;
                try {
                    keyV = (K) field.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List转GroupMap失败, ex", ex);
                }
                return keyV;
            }));

        } catch (Exception e) {
            LogUtils.logError("Util List转GroupMap失败, e", e);
        }

        field.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List转MapGroupMap - 归类Map
     *
     * @param entityList list数据
     * @param key1       主键1
     * @param key2       主键2
     * @param <K1>       key1类型
     * @param <K2>       key1类型
     * @param <V>        value类型
     * @return 返回MapGroupMap
     */
    @SuppressWarnings("unchecked")
    public static <K1, K2, V> Map<K1, Map<K2, List<V>>> listToMapGroupMap(List<V> entityList, String key1, String key2) {
        // 返回值准备
        Map<K1, Map<K2, List<V>>> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field1 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key1);
        Field field2 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key2);
        if (Objects.isNull(field1) || Objects.isNull(field2)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field1.setAccessible(Boolean.TRUE);
        field2.setAccessible(Boolean.TRUE);

        String errorMsg = "Util List转MapGroupMap失败, ex";
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                    return false;
                }
            }).collect(Collectors.groupingBy(e -> {
                K1 key1V = null;
                try {
                    key1V = (K1) field1.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key1V;
            }, Collectors.groupingBy(e -> {
                K2 key2V = null;
                try {
                    key2V = (K2) field2.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key2V;
            })));

        } catch (Exception e) {
            LogUtils.logError(errorMsg, e);
        }

        field1.setAccessible(Boolean.FALSE);
        field2.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List根据字段去重
     *
     * @param entityList list数据
     * @param key        主键
     * @param <V>        value类型
     * @return GroupMap
     */
    public static <V> List<V> listDistinctByField(List<V> entityList, String key) {
        // 返回值准备
        List<V> result = new ArrayList<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key);
        if (Objects.isNull(field)) {
            return result;
        }

        // 使用Java Stream转化为Map
        field.setAccessible(Boolean.TRUE);

        try {
            result = entityList.stream().filter(
                    distinctByKey(e -> {
                        try {
                            return field.get(e);
                        } catch (IllegalAccessException ex) {
                            LogUtils.logError("Util List根据字段去重失败, ex", ex);
                            return false;
                        }
                    })
            ).collect(Collectors.toList());

        } catch (Exception e) {
            LogUtils.logError("Util List根据字段去重失败, e", e);
        }

        field.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List根据字段去重 - 多字段
     *
     * @param entityList list数据
     * @param keys       主键s
     * @param <V>        value类型
     * @return GroupMap
     */
    public static <V> List<V> listDistinctByFields(List<V> entityList, String[] keys) {
        // 返回值准备
        List<V> result = new ArrayList<>();
        if (Util.isNull(entityList) || Util.isNull(keys)) {
            return result;
        }

        // 获取字段对应的映射
        Map<String, Field> fields = ReflectionUtil.listAllField(entityList.get(0).getClass());
        List<Field> fieldList = new ArrayList<>();
        for (String key : keys) {
            Field field = fields.get(key);
            if (Objects.nonNull(field)) {
                field.setAccessible(Boolean.TRUE);
                fieldList.add(field);
            }
        }
        if (Util.isNull(fieldList)) {
            return result;
        }

        // 开始去重
        try {
            result = entityList.stream().filter(
                    distinctByKey(e -> {
                        try {
                            StringBuilder distinctKey = new StringBuilder();
                            for (Field field : fieldList) {
                                distinctKey.append(field.get(e));
                            }
                            return distinctKey.toString();
                        } catch (IllegalAccessException ex) {
                            LogUtils.logError("Util List根据多字段去重失败, ex", ex);
                            return false;
                        }
                    })
            ).collect(Collectors.toList());

        } catch (Exception e) {
            LogUtils.logError("Util List根据多字段去重失败, e", e);
        }

        // 关闭访问权限
        fieldList.forEach(field -> field.setAccessible(Boolean.FALSE));


        return result;
    }

    /**
     * stream 按照字段去重
     *
     * @param keyExtractor 方法
     * @param <T>          泛型
     * @return 方法
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        HashMap<Object, Boolean> map = new HashMap<>(16);
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * List根据某个字段求和
     *
     * @param entityList list数据
     * @param key        主键
     * @return 和
     */
    public static <V> BigDecimal sumList(List<V> entityList, String key) {
        // 返回值准备
        BigDecimal result = BigDecimal.ZERO;
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key);
        if (Objects.isNull(field)) {
            return result;
        }

        // 开始权限
        field.setAccessible(Boolean.TRUE);

        // 使用Java Stream转化为Map
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List根据某个字段求和, ex", ex);
                    return false;
                }
            }).map(e -> {
                BigDecimal keyV = BigDecimal.ZERO;
                try {
                    keyV = (BigDecimal) field.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List根据某个字段求和, ex", ex);
                }
                return keyV;
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (Exception e) {
            LogUtils.logError("Util List根据某个字段求和, e", e);
        }

        // 关闭权限
        field.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List根据某个字段求平均值
     *
     * @param entityList list数据
     * @param key        主键
     * @return 平均值
     */
    public static <V> BigDecimal averageList(List<V> entityList, String key) {
        // 返回值准备
        BigDecimal result = BigDecimal.ZERO;
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key);
        if (Objects.isNull(field)) {
            return result;
        }

        // 开始权限
        field.setAccessible(Boolean.TRUE);

        // 使用Java Stream转化为Map
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List根据某个字段品均值, ex", ex);
                    return false;
                }
            }).map(e -> {
                BigDecimal keyV = BigDecimal.ZERO;
                try {
                    keyV = (BigDecimal) field.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError("Util List根据某个字段品均值, ex", ex);
                }
                return keyV;
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (Exception e) {
            LogUtils.logError("Util List根据某个字段品均值, e", e);
        }

        // 关闭权限
        field.setAccessible(Boolean.FALSE);

        return result.divide(BigDecimal.valueOf(entityList.size()), 6, RoundingMode.HALF_UP);
    }

    /**
     * List归类求和
     *
     * @param entityList list数据
     * @param key1       主键1
     * @param key2       主键2
     * @param <K1>       key1类型
     * @param <V>        value类型
     * @return 归类和
     */
    @SuppressWarnings("unchecked")
    public static <K1, V> Map<K1, BigDecimal> sumListToGroupMap(List<V> entityList, String key1, String key2) {
        // 返回值准备
        Map<K1, BigDecimal> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field1 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key1);
        Field field2 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key2);
        if (Objects.isNull(field1) || Objects.isNull(field2)) {
            return result;
        }

        // 开始权限
        field1.setAccessible(Boolean.TRUE);
        field2.setAccessible(Boolean.TRUE);

        // 使用Java Stream分类求和
        String errorMsg = "Util List归类求和, ex";
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                    return false;
                }
            }).collect(Collectors.groupingBy(e -> {
                K1 key1V = null;
                try {
                    key1V = (K1) field1.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key1V;
            }, Collectors.reducing(BigDecimal.ZERO, e -> {
                BigDecimal key2V = BigDecimal.ZERO;
                try {
                    key2V = (BigDecimal) field2.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key2V;
            }, BigDecimal::add)));

        } catch (Exception e) {
            LogUtils.logError(errorMsg, e);
        }

        // 关闭权限
        field1.setAccessible(Boolean.FALSE);
        field2.setAccessible(Boolean.FALSE);

        return result;
    }

    /**
     * List归类求平均值
     *
     * @param entityList list数据
     * @param key1       主键1
     * @param key2       主键2
     * @param <K1>       key1类型
     * @param <V>        value类型
     * @return 归类平均值
     */
    @SuppressWarnings("unchecked")
    public static <K1, V> Map<K1, BigDecimal> averageListToGroup(List<V> entityList, String key1, String key2) {
        // 返回值准备
        Map<K1, BigDecimal> result = new HashMap<>();
        if (Util.isNull(entityList)) {
            return result;
        }

        // 获取字段对应的映射
        Field field1 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key1);
        Field field2 = ReflectionUtil.getFiledByName(entityList.get(0).getClass(), key2);
        if (Objects.isNull(field1) || Objects.isNull(field2)) {
            return result;
        }

        // 开始权限
        field1.setAccessible(Boolean.TRUE);
        field2.setAccessible(Boolean.TRUE);

        // 使用Java Stream分类求和
        String errorMsg = "Util List归类平均, ex";
        try {
            result = entityList.stream().filter(e -> {
                try {
                    return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                    return false;
                }
            }).collect(Collectors.groupingBy(e -> {
                K1 key1V = null;
                try {
                    key1V = (K1) field1.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key1V;
            }, Collectors.reducing(BigDecimal.ZERO, e -> {
                BigDecimal key2V = BigDecimal.ZERO;
                try {
                    key2V = (BigDecimal) field2.get(e);
                } catch (IllegalAccessException ex) {
                    LogUtils.logError(errorMsg, ex);
                }
                return key2V;
            }, BigDecimal::add)));

        } catch (Exception e) {
            LogUtils.logError(errorMsg, e);
        }

        // 关闭权限
        field1.setAccessible(Boolean.FALSE);
        field2.setAccessible(Boolean.FALSE);

        // 归类获取总数
        Map<K1, Long> resultCountGroupMap = entityList.stream().filter(e -> {
            try {
                return Objects.nonNull(e) && Objects.nonNull(field1.get(e)) && Objects.nonNull(field2.get(e));
            } catch (IllegalAccessException ex) {
                LogUtils.logError(errorMsg, ex);
                return false;
            }
        }).collect(Collectors.groupingBy(e -> {
            K1 key1V = null;
            try {
                key1V = (K1) field1.get(e);
            } catch (IllegalAccessException ex) {
                LogUtils.logError(errorMsg, ex);
            }
            return key1V;
        }, Collectors.counting()));

        // 计算平均值
        for (Map.Entry<K1, BigDecimal> entry : result.entrySet()) {
            K1 k = entry.getKey();
            BigDecimal sum = entry.getValue();
            Long count = resultCountGroupMap.get(k);
            if (Objects.isNull(count) || count <= 0) {
                continue;
            }

            result.put(k, sum.divide(BigDecimal.valueOf(count), 6, RoundingMode.HALF_UP));
        }

        return result;
    }

    /**
     * 下划线转驼峰
     *
     * @param str 带下划线字符串
     * @return 驼峰字符串
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param str 驼峰字符串
     * @return 带下划线字符串
     */
    public static String humpToLine(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取不存在的实体List
     *
     * @param sources   源实体s
     * @param existList 已存在的实体List
     * @param <T>       泛型
     * @return 不存在的实体List
     */
    public static <T> List<T> getNotInList(T[] sources, List<T> existList) {
        if (Util.isNull(sources)) {
            return new ArrayList<>();
        }

        if (Util.isNull(existList)) {
            return Arrays.stream(sources).collect(Collectors.toList());
        }

        List<T> result = new ArrayList<>();
        for (T e : sources) {
            if (!existList.contains(e)) {
                result.add(e);
            }
        }

        return result;
    }

    /**
     * 格式化cols - 兼容架构的cols:["id","name"...]类型的传参。把[、]、"等符号去掉
     *
     * @param cols :["id","name", ...]
     * @return cols :[id, name, ...]
     */
    public static String[] formatCols(String[] cols) {
        if (Util.isNull(cols)) {
            return cols;
        }

        // 开始做兼容操作， 字符串替换
        for (int i = 0; i < cols.length; ++i) {
            if (Util.isNotString(cols[i])) {
                continue;
            }

            cols[i] = cols[i].replace("[", "")
                    .replace("]", "")
                    .replace("\'", "")
                    .replace("\"", "");
        }

        return Arrays.stream(cols).filter(Util::isString).distinct().toArray(String[]::new);
    }

    /**
     * 获取两个Entity之前不同信息的字段，并以字符串形式打印
     * 注意：用于记录修改日志时，快速获取修改的内容。
     * 只判断在PropertyTypeEnum类型中的类型字段，并且只有被EntityFieldInfo注解的字段。
     * 如果newValue（修改的值）是null，默认不认为是修改，只有少数Date类型的数据需要修改为null。
     *
     * @param oldEntity 旧Entity
     * @param newEntity 新Entity
     * @param <T>       泛型
     * @return 不同信息字符串
     */
    public static <T> String getChangeFieldInfo(T oldEntity, T newEntity) {
        if (Objects.isNull(newEntity) || Objects.isNull(oldEntity)) {
            return "";
        }

        // 返回字符准备
        StringBuilder stringBuilder = new StringBuilder();
        // 反射MAP准备
        Map<String, Field> fieldMap;

        String modifiedMsg = "\"修改为\"";
        int maxModifyLength = 20;
        try {
            // 获取全部的反射字段
            fieldMap = ReflectionUtil.listAllField(newEntity.getClass());

            // 开始遍历字段，添加变化的信息
            for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
                String fieldName = entry.getKey();
                Field field = entry.getValue();

                // 获取属性类型 - 只判断在PropertyTypeEnum类型中的类型字段
                String type = field.getType().getTypeName();
                PropertyTypeEnum propertyTypeEnum = PropertyTypeEnum.getEnumByName(type);
                if (propertyTypeEnum == null) {
                    continue;
                }

                // 获取字段注解信息
                EntityFieldInfo fieldInfo = field.getAnnotation(EntityFieldInfo.class);
                if (Objects.isNull(fieldInfo)) {
                    continue;
                }

                // 打开私有访问
                field.setAccessible(Boolean.TRUE);
                // 获取属性值
                Object newValue = field.get(newEntity);
                // 获取属性值
                Object oldValue = field.get(oldEntity);
                // 关闭私有访问
                field.setAccessible(Boolean.FALSE);

                switch (propertyTypeEnum) {
                    case BYTE:
                        if (Objects.isNull(newValue) || Objects.equals(newValue, oldValue)) {
                            break;
                        }
                        stringBuilder.append(fieldInfo.desc()).append("由\"");

                        // 判断是否为"是否"字段
                        if (fieldName.startsWith("is")) {
                            // 修改前
                            if (BaseConstant.YES_BYTE.equals(oldValue)) {
                                stringBuilder.append(BaseConstant.YES_CN);
                            } else {
                                stringBuilder.append(BaseConstant.NO_CN);
                            }

                            stringBuilder.append(modifiedMsg);

                            // 修改后
                            if (BaseConstant.YES_BYTE.equals(newValue)) {
                                stringBuilder.append(BaseConstant.YES_CN);
                            } else {
                                stringBuilder.append(BaseConstant.NO_CN);
                            }
                        } else {
                            // 修改前
                            if (Objects.nonNull(oldValue)) {
                                stringBuilder.append(oldValue.toString());
                            } else {
                                stringBuilder.append(" ");
                            }

                            // 修改后
                            stringBuilder.append(modifiedMsg);
                            stringBuilder.append(newValue.toString());
                        }
                        stringBuilder.append("\",");

                        break;
                    case DATE:
                        if (DateUtil.equalDate((Date) oldValue, (Date) newValue) || Objects.isNull(newValue)) {
                            break;
                        }

                        stringBuilder.append(fieldInfo.desc()).append("由\"");
                        stringBuilder.append(DateUtil.getDateTimeStr((Date) oldValue))
                                .append(modifiedMsg).append(DateUtil.getDateTimeStr((Date) newValue)).append("\",");
                        break;
                    case BIG_DECIMAL:
                        if (Util.equalBigDecimal((BigDecimal) oldValue, (BigDecimal) newValue) || Objects.isNull(newValue)) {
                            break;
                        }

                        stringBuilder.append(fieldInfo.desc()).append("由\"");
                        if (Objects.nonNull(oldValue)) {
                            stringBuilder.append(((BigDecimal) oldValue).stripTrailingZeros().toPlainString());
                        } else {
                            stringBuilder.append(" ");
                        }

                        stringBuilder.append(modifiedMsg);
                        stringBuilder.append(((BigDecimal) newValue).stripTrailingZeros().toPlainString());
                        stringBuilder.append("\",");

                        break;
                    default:
                        if (Objects.equals(newValue, oldValue) || Objects.isNull(newValue)) {
                            break;
                        }

                        String oldString = "";
                        String newString = newValue.toString();
                        if (Objects.nonNull(oldValue)) {
                            oldString = oldValue.toString();
                        }

                        // 如果字符串大于最大修改长度50，则不记录变化情况，只记录是否变化
                        if (oldString.length() > maxModifyLength || newString.length() > maxModifyLength) {
                            stringBuilder.append("被修改,");
                        } else {
                            stringBuilder.append(fieldInfo.desc()).append("由\"");
                            stringBuilder.append(oldString);
                            stringBuilder.append(modifiedMsg);
                            stringBuilder.append(newString);
                            stringBuilder.append("\",");
                        }

                        break;
                }
            }

        } catch (Exception e) {
            LogUtils.logError("获取两个Entity之间不同信息的的字符串 e:", e);
            return "";
        }

        return stringBuilder.length() > 0 ? stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString() : stringBuilder.toString();
    }
}
