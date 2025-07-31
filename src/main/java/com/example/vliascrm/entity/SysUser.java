package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 系统用户实体类
 */
@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity implements UserDetails {

    /**
     * 用户名
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 头像URL
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Column
    private Integer gender;

    /**
     * 手机号
     */
    @Column(length = 20)
    private String mobile;

    /**
     * 邮箱
     */
    @Column(length = 100)
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
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 员工编号
     */
    @Column(name = "emp_no", length = 50)
    private String empNo;
    
    /**
     * 用户类型
     */
    @Column(name = "user_type", length = 50)
    private String userType;
    
    /**
     * 用户权限列表（非持久化字段）
     */
    @Transient
    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    /**
     * 设置用户权限
     * @param authorities 权限列表
     */
    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
    }
} 