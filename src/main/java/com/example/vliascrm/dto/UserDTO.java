package com.example.vliascrm.dto;

import com.example.vliascrm.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户DTO，扩展SysUser，增加组织、部门和岗位名称
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {
    
    /**
     * 所属组织名称
     */
    private String orgName;
    
    /**
     * 所属部门名称
     */
    private String deptName;
    
    /**
     * 所属岗位名称
     */
    private String positionName;
    
    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    private Boolean isDeleted;
    
    /**
     * 从SysUser创建UserDTO
     * @param user 用户实体
     * @return UserDTO
     */
    public static UserDTO fromSysUser(SysUser user) {
        UserDTO dto = new UserDTO();
        // 复制基础字段
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setAvatar(user.getAvatar());
        dto.setGender(user.getGender());
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());
        dto.setOrgId(user.getOrgId());
        dto.setDeptId(user.getDeptId());
        dto.setPositionId(user.getPositionId());
        dto.setStatus(user.getStatus());
        dto.setLastLoginTime(user.getLastLoginTime());
        dto.setEmpNo(user.getEmpNo());
        dto.setUserType(user.getUserType());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        
        // 密码置空
        dto.setPassword(null);
        
        return dto;
    }
} 