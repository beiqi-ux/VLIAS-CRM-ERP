package com.example.vliascrm.entity.operation;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 任务日志表实体类
 */
@Data
@Entity
@Table(name = "sys_task_log")
@DynamicInsert
@DynamicUpdate
public class SysTaskLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 日志ID
    
    @Column(nullable = false)
    private Long taskId; // 任务ID
    
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
    
    private Long executionTime; // 执行耗时(毫秒)
    
    private Integer status; // 状态 0-失败 1-成功
    
    @Lob
    private String errorMsg; // 错误消息
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
} 