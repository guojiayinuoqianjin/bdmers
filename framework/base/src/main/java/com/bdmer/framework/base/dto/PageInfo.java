package com.bdmer.framework.base.dto;

/**
 * 分页信息
 *
 * @author GongDeLang
 * @since 2020/6/1 17:30
 */
public class PageInfo {
    /** 页码，默认0，显示第一页 **/
    private int pageIndex = 0;

    /** 分页偏移量 **/
    private int offset;

    /** 每页显示大小，默认50条记录 **/
    private int pageSize = 50;

    /** 排序方式，默认升序 **/
    private String sortOrder = Sort.ASC.toString();

    /** 排序字段 **/
    private String sortField;

    /** 总条数 **/
    private Integer total;

    public enum Sort {
        /** 降序 **/
        DESC, ASC
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getOffset() {
        this.offset = pageIndex * pageSize;
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
