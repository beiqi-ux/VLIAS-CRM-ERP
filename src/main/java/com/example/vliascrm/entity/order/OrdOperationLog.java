package com.example.vliascrm.entity.order;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 订单操作日志表实体类
 */
@Data
@Entity
@Table(name = "ord_operation_log")
@EntityListeners(AuditingEntityListener.class)
public class OrdOperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 操作类型
     */
    @Column(name = "operation_type", nullable = false)
    private Integer operationType;

    /**
     * 操作内容
     */
    @Column(name = "operation_content", nullable = false, length = 500)
    private String operationContent;

    /**
     * 操作人ID
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * 操作人类型 1-系统 2-用户 3-管理员
     */
    @Column(name = "operator_type", columnDefinition = "tinyint default 1")
    private Integer operatorType = 1;

    /**
     * 操作人名称
     */
    @Column(name = "operator_name", length = 50)
    private String operatorName;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 