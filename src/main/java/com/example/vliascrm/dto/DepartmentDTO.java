package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门DTO
 */
@Data
public class DepartmentDTO {
    
    private Long id;
    private Long orgId;
    private String deptName;
    private String deptCode;
    private Long parentId;
    private String leader;
    private String phone;
    private String email;
    private Integer status;
    private Integer sort;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    private Boolean isDeleted;
    
    // 扩展字段
    private String orgName;
    private String parentName;
    private List<DepartmentDTO> children;
    private Boolean hasChildren;
} 