package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 数据字典项实体类
 */
@Data
@Entity
@Table(name = "sys_dict_item", indexes = {
    @Index(name = "idx_dict_id", columnList = "dict_id")
})
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends BaseEntity {

    /**
     * 字典ID
     */
    @Column(name = "dict_id", nullable = false)
    private Long dictId;

    /**
     * 字典项文本
     */
    @Column(name = "item_text", nullable = false, length = 50)
    private String itemText;

    /**
     * 字典项值
     */
    @Column(name = "item_value", nullable = false, length = 50)
    private String itemValue;

    /**
     * 字典项描述
     */
    @Column(name = "description", length = 200)
    private String description;

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
} 