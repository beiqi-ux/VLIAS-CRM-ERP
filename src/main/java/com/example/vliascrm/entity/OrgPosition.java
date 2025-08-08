package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 岗位实体类
 */
@Data
@Entity
@Table(name = "org_position")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgPosition extends BaseEntity {

    /**
     * 所属组织ID
     */
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    /**
     * 岗位名称
     */
    @Column(name = "position_name", nullable = false, length = 50)
    private String positionName;

    /**
     * 岗位编码
     */
    @Column(name = "position_code", nullable = false, length = 50)
    private String positionCode;

    /**
     * 所属部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;

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