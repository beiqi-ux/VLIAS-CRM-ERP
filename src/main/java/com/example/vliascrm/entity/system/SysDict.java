package com.example.vliascrm.entity.system;

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
@Table(name = "sys_dict")
@EqualsAndHashCode(callSuper = true)
public class SysDict extends BaseEntity {

    /**
     * 字典名称
     */
    @Column(name = "dict_name", nullable = false, length = 50)
    private String dictName;

    /**
     * 字典编码
     */
    @Column(name = "dict_code", nullable = false, length = 50, unique = true)
    private String dictCode;

    /**
     * 字典描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;
} 