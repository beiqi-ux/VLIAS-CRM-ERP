package com.example.vliascrm.entity.mobile;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户设置实体类
 */
@Data
@Entity
@Table(name = "app_setting")
@EqualsAndHashCode(callSuper = true)
public class AppSetting extends BaseEntity {

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 推送开关 0-关闭 1-开启
     */
    @Column(name = "push_switch", columnDefinition = "tinyint default 1")
    private Integer pushSwitch = 1;

    /**
     * 声音开关 0-关闭 1-开启
     */
    @Column(name = "sound_switch", columnDefinition = "tinyint default 1")
    private Integer soundSwitch = 1;

    /**
     * 震动开关 0-关闭 1-开启
     */
    @Column(name = "vibrate_switch", columnDefinition = "tinyint default 1")
    private Integer vibrateSwitch = 1;

    /**
     * 主题
     */
    @Column(name = "theme", length = 20, columnDefinition = "varchar(20) default 'default'")
    private String theme = "default";

    /**
     * 语言
     */
    @Column(name = "language", length = 10, columnDefinition = "varchar(10) default 'zh_CN'")
    private String language = "zh_CN";

    /**
     * 货币
     */
    @Column(name = "currency", length = 10, columnDefinition = "varchar(10) default 'CNY'")
    private String currency = "CNY";

    /**
     * 字体大小
     */
    @Column(name = "font_size", length = 10, columnDefinition = "varchar(10) default 'medium'")
    private String fontSize = "medium";

    /**
     * 隐私设置JSON
     */
    @Column(name = "privacy_settings", columnDefinition = "text")
    private String privacySettings;
} 