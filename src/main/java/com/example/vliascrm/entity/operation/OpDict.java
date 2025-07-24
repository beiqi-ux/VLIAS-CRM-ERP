package com.example.vliascrm.entity.operation;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典实体类
 */
@Data
@Entity
@Table(name = "op_dict")
@EqualsAndHashCode(callSuper = true)
public class OpDict extends BaseEntity {

    /**
     * 字典类型
     */
    @Column(name = "dict_type", nullable = false, length = 100)
    private String dictType;

    /**
     * 字典标签
     */
    @Column(name = "dict_label", nullable = false, length = 100)
    private String dictLabel;

    /**
     * 字典值
     */
    @Column(name = "dict_value", nullable = false, length = 100)
    private String dictValue;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
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