package com.example.vliascrm.entity.operation;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 定时任务表实体类
 */
@Data
@Entity
@Table(name = "sys_task")
@DynamicInsert
@DynamicUpdate
public class SysTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 任务ID
    
    @Column(nullable = false, length = 100)
    private String taskName; // 任务名称
    
    @Column(nullable = false, length = 50)
    private String taskGroup; // 任务组名
    
    @Column(nullable = false, length = 255)
    private String taskClass; // 任务类
    
    @Column(nullable = false, length = 100)
    private String taskMethod; // 任务方法
    
    @Column(length = 500)
    private String taskParams; // 任务参数
    
    @Column(nullable = false, length = 50)
    private String cronExpression; // cron表达式
    
    private Integer status; // 状态 0-停用 1-正常
    
    @Lob
    private String remark; // 备注
    
    private Integer concurrent; // 是否并发 0-否 1-是
    
    private Integer executeTimes; // 已执行次数
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastExecuteTime; // 上次执行时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextExecuteTime; // 下次执行时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
    
    private Long createBy; // 创建人
    
    private Long updateBy; // 更新人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 