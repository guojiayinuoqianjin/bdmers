package com.bdmer.server.tbk.dto;

import com.bdmer.server.tbk.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * 用户 - FilterDTO
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFilterDTO extends UserEntity {
    // 基础字段
    /**
     * 查询字段
     */
    private String[] cols;
    /**
     * 分页信息 - index
     */
    private Integer pageIndex;
    /**
     * 分页信息 - size
     */
    private Integer pageSize;
    /**
     * 分页信息 - offset
     */
    private Integer offset;
    /**
     * 是否需要已经逻辑删除的数据 - 默认不需要（需要的话请设置为true）
     */
    private Boolean withDeleted;
    /**
     * 排序字段
     */
    private String[] sortFields;
    /**
     * 排序方式 - ASC、DESC
     */
    private String sortOrder;
    /**
     * 标识s
     */
    private Long[] ids;

    // 扩展查询字段
    /**
     * 创建时间- 开始时间
     */
    private String gmtCreateBegin;
    /**
     * 创建时间- 结束时间
     */
    private String gmtCreateEnd;


    /**
     * 获取offset
     *
     * @return offest
     */
    public Integer getOffset() {
        if (Objects.isNull(this.pageSize)) {
            return 0;
        }
        if (Objects.isNull(this.pageIndex)) {
            this.pageIndex = 0;
        }

        this.offset = this.pageIndex * this.pageSize;
        return this.offset;
    }
}