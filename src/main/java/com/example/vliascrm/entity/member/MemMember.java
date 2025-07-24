package com.example.vliascrm.entity.member;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员实体类
 */
@Data
@Entity
@Table(name = "mem_member")
@EqualsAndHashCode(callSuper = true)
public class MemMember extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 255)
    private String avatar;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Column(name = "gender", columnDefinition = "tinyint default 0")
    private Integer gender = 0;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * 手机号
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 会员等级ID
     */
    @Column(name = "level_id", columnDefinition = "bigint default 1")
    private Long levelId = 1L;

    /**
     * 积分
     */
    @Column(name = "points", columnDefinition = "int default 0")
    private Integer points = 0;

    /**
     * 成长值
     */
    @Column(name = "growth", columnDefinition = "int default 0")
    private Integer growth = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 来源 1-PC 2-APP 3-小程序 4-H5
     */
    @Column(name = "source")
    private Integer source;

    /**
     * 邀请码
     */
    @Column(name = "invite_code", length = 20)
    private String inviteCode;

    /**
     * 邀请人ID
     */
    @Column(name = "inviter_id")
    private Long inviterId;

    /**
     * 累计消费金额
     */
    @Column(name = "total_order_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalOrderAmount = BigDecimal.ZERO;

    /**
     * 订单数量
     */
    @Column(name = "order_count", columnDefinition = "int default 0")
    private Integer orderCount = 0;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;

    /**
     * 注册时间
     */
    @Column(name = "register_time", nullable = false)
    private LocalDateTime registerTime;

    /**
     * 注册IP
     */
    @Column(name = "register_ip", length = 50)
    private String registerIp;
} 