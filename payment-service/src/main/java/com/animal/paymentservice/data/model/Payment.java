package com.animal.paymentservice.data.model;

import com.animal.common.status.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@Builder
@Table
public class Payment {
    @Id
    @Column("payment_id")
    private String paymentId;
    @Column("application_id")
    private String applicationId;
    @Column("user_profile_id")
    private String userProfileId;
    @Column("customer_id")
    private String customerId;
    @Column("payment_intent_id")
    private String paymentIntentId;
    @Column("payment_status")
    private PaymentStatus paymentStatus;
    @Version
    private Long version;
    @CreatedBy
    @Column("created_by")
    private String createdBy;
    @CreatedDate
    @Column("created_date")
    private Long createdDate;
    @LastModifiedDate
    @Column("last_modified_date")
    private Long lastModifiedDate;
}
