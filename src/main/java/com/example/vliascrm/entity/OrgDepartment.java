package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 部门实体类
 */
@Data
@Entity
@Table(name = "org_department")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDepartment extends BaseEntity {

    /**
     * 所属组织ID
     */
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    /**
     * 部门名称
     */
    @Column(name = "dept_name", nullable = false, length = 50)
    private String deptName;

    /**
     * 部门编码
     */
    @Column(name = "dept_code", nullable = false, length = 50)
    private String deptCode;

    /**
     * 父部门ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 部门负责人
     */
    @Column(length = 50)
    private String leader;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status;

    /**
     * 排序
     */
    @Column
    private Integer sort;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remark;
} 