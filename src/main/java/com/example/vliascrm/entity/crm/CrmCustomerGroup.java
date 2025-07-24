package com.example.vliascrm.entity.crm;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户分组关联实体类
 */
@Data
@Entity
@Table(name = "crm_customer_group", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"customer_id", "group_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class CrmCustomerGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 分组ID
     */
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "create_by")
    private Long createBy;
} 