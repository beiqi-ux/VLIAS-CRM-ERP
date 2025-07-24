package com.example.vliascrm.entity.i18n;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语言实体类
 */
@Data
@Entity
@Table(name = "sys_language")
@EqualsAndHashCode(callSuper = true)
public class SysLanguage extends BaseEntity {

    /**
     * 语言代码
     */
    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    /**
     * 语言名称
     */
    @Column(name = "language_name", nullable = false, length = 50)
    private String languageName;

    /**
     * 语言图标
     */
    @Column(name = "icon", length = 255)
    private String icon;

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
     * 是否默认 0-否 1-是
     */
    @Column(name = "is_default", columnDefinition = "tinyint default 0")
    private Integer isDefault = 0;

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