package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织机构DTO
 */
@Data
public class OrganizationDTO {
    
    private Long id;
    private Long parentId;
    private String orgName;
    private String orgCode;
    private Integer orgType;
    private String leader;
    private String phone;
    private String email;
    private String address;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    private Boolean isDeleted;
    
    // 扩展字段
    private String parentName;
    private List<OrganizationDTO> children;
    private Boolean hasChildren;
} 