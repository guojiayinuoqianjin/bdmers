package com.bdmer.framework.base.cmomon.constant;

/**
 * mysql操作常量
 *
 * @author GongDeLang
 * @since 2020/4/24 16:29
 */
public class MysqlConstant {
    private MysqlConstant() {
        throw new IllegalStateException("MysqlConstant class");
    }

    /**
     * 最大插入数量
     */
    public static final Integer MAX_INSERT_NUM = 1000;
    /**
     * 最大更新数量
     */
    public static final Integer MAX_UPDATE_NUM = 1000;
    /**
     * 最大延时时间
     */
    public static final Integer MAX_SLEEP_TIME = 10;
    /**
     * 排序方式
     */
    public static final String ORDER_BY_ASC = "ASC";
    public static final String ORDER_BY_DESC = "DESC";

    /**
     * 最大PageIndex
     */
    public static final Integer MAX_PAGE_INDEX = 100000;

}
