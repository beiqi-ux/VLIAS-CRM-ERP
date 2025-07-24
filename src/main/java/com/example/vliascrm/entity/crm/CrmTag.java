package com.example.vliascrm.entity.crm;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户标签实体类
 */
@Data
@Entity
@Table(name = "crm_tag")
@EqualsAndHashCode(callSuper = true)
public class CrmTag extends BaseEntity {

    /**
     * 标签名称
     */
    @Column(name = "tag_name", nullable = false, length = 50, unique = true)
    private String tagName;

    /**
     * 标签颜色
     */
    @Column(name = "tag_color", length = 20)
    private String tagColor;
} 