package com.example.vliascrm.entity.crm;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户分组实体类
 */
@Data
@Entity
@Table(name = "crm_group")
@EqualsAndHashCode(callSuper = true)
public class CrmGroup extends BaseEntity {

    /**
     * 分组名称
     */
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    /**
     * 父分组ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint default 0")
    private Long parentId = 0L;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;
} 