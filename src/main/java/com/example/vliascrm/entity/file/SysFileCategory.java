package com.example.vliascrm.entity.file;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件分类实体类
 */
@Data
@Entity
@Table(name = "sys_file_category")
@EqualsAndHashCode(callSuper = true)
public class SysFileCategory extends BaseEntity {

    /**
     * 分类名称
     */
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    /**
     * 分类编码
     */
    @Column(name = "category_code", length = 50)
    private String categoryCode;

    /**
     * 父分类ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint default 0")
    private Long parentId = 0L;

    /**
     * 层级路径，格式：1,2,3
     */
    @Column(name = "path", length = 255)
    private String path;

    /**
     * 层级
     */
    @Column(name = "level", columnDefinition = "int default 1")
    private Integer level = 1;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;
} 