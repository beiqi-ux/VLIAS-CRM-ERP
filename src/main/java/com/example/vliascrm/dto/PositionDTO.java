package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 岗位DTO
 */
@Data
public class PositionDTO {
    
    private Long id;
    private Long orgId;
    private String positionName;
    private String positionCode;
    private Long deptId;
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
    private String deptName;
} 