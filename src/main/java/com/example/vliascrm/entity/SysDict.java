package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

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
    @Column(name = "dict_code", unique = true, nullable = false, length = 50)
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