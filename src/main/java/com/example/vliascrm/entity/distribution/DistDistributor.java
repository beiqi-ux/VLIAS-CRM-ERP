package com.example.vliascrm.entity.distribution;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分销员实体类
 */
@Data
@Entity
@Table(name = "dist_distributor")
@EqualsAndHashCode(callSuper = true)
public class DistDistributor extends BaseEntity {

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 分销员姓名
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 手机号
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 分销等级ID
     */
    @Column(name = "level_id", columnDefinition = "bigint default 1")
    private Long levelId = 1L;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 邀请码
     */
    @Column(name = "invite_code", nullable = false, length = 20)
    private String inviteCode;

    /**
     * 上级分销员ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 上级路径ID，用逗号分隔
     */
    @Column(name = "parent_path", length = 500)
    private String parentPath;

    /**
     * 累计佣金
     */
    @Column(name = "total_commission", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalCommission = BigDecimal.ZERO;

    /**
     * 可用佣金
     */
    @Column(name = "available_commission", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal availableCommission = BigDecimal.ZERO;

    /**
     * 已提现佣金
     */
    @Column(name = "withdrawn_commission", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal withdrawnCommission = BigDecimal.ZERO;

    /**
     * 冻结佣金
     */
    @Column(name = "freezing_commission", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal freezingCommission = BigDecimal.ZERO;

    /**
     * 团队人数
     */
    @Column(name = "team_count", columnDefinition = "int default 0")
    private Integer teamCount = 0;

    /**
     * 一级人数
     */
    @Column(name = "first_level_count", columnDefinition = "int default 0")
    private Integer firstLevelCount = 0;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private LocalDateTime applyTime;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;
} 