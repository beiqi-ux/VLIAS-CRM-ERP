package com.example.vliascrm.entity.mobile;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 推送记录实体类
 */
@Data
@Entity
@Table(name = "app_push")
@EqualsAndHashCode(callSuper = true)
public class AppPush extends BaseEntity {

    /**
     * 推送标题
     */
    @Column(name = "push_title", nullable = false, length = 100)
    private String pushTitle;

    /**
     * 推送内容
     */
    @Column(name = "push_content", nullable = false, columnDefinition = "text")
    private String pushContent;

    /**
     * 推送类型 1-系统消息 2-活动消息 3-订单消息 4-其他
     */
    @Column(name = "push_type", nullable = false)
    private Integer pushType;

    /**
     * 推送平台 1-全平台 2-Android 3-iOS 4-小程序
     */
    @Column(name = "push_platform", nullable = false)
    private Integer pushPlatform;

    /**
     * 推送范围 1-全部用户 2-指定用户 3-指定标签
     */
    @Column(name = "push_scope", nullable = false)
    private Integer pushScope;

    /**
     * 目标ID集合，多个以逗号分隔
     */
    @Column(name = "target_ids", length = 500)
    private String targetIds;

    /**
     * 计划推送时间
     */
    @Column(name = "plan_time")
    private LocalDateTime planTime;

    /**
     * 实际推送时间
     */
    @Column(name = "push_time")
    private LocalDateTime pushTime;

    /**
     * 推送状态 1-待推送 2-推送中 3-推送成功 4-推送失败 5-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 发送总数
     */
    @Column(name = "total_count", columnDefinition = "int default 0")
    private Integer totalCount = 0;

    /**
     * 成功数量
     */
    @Column(name = "success_count", columnDefinition = "int default 0")
    private Integer successCount = 0;

    /**
     * 失败数量
     */
    @Column(name = "fail_count", columnDefinition = "int default 0")
    private Integer failCount = 0;

    /**
     * 点击数量
     */
    @Column(name = "click_count", columnDefinition = "int default 0")
    private Integer clickCount = 0;

    /**
     * 推送提供商 1-极光 2-个推 3-小米 4-华为 5-OPPO 6-VIVO 7-苹果
     */
    @Column(name = "provider")
    private Integer provider;

    /**
     * 失败原因
     */
    @Column(name = "fail_reason", columnDefinition = "text")
    private String failReason;

    /**
     * 是否定时 0-否 1-是
     */
    @Column(name = "is_timing", columnDefinition = "tinyint default 0")
    private Integer isTiming = 0;

    /**
     * 附加数据
     */
    @Column(name = "extra_data", columnDefinition = "text")
    private String extraData;

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