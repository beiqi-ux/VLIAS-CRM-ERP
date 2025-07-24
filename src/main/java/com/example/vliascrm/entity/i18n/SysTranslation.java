package com.example.vliascrm.entity.i18n;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 翻译实体类
 */
@Data
@Entity
@Table(name = "sys_translation")
@EqualsAndHashCode(callSuper = true)
public class SysTranslation extends BaseEntity {

    /**
     * 语言代码
     */
    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    /**
     * 翻译键
     */
    @Column(name = "translation_key", nullable = false, length = 100)
    private String translationKey;

    /**
     * 翻译内容
     */
    @Column(name = "translation_value", columnDefinition = "text")
    private String translationValue;

    /**
     * 模块
     */
    @Column(name = "module", length = 50)
    private String module;

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