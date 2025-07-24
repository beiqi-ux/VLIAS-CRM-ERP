package com.example.vliascrm.entity.system;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统用户实体类
 */
@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

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
    private Integer gender;
    
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
     * 所属组织ID
     */
    @Column(name = "org_id")
    private Long orgId;
    
    /**
     * 所属部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;
    
    /**
     * 岗位ID
     */
    @Column(name = "position_id")
    private Long positionId;
    
    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;
    
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
} 