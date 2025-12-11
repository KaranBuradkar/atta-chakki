package com.atachakki.components.due.or.refund;

import com.atachakki.components.customer.Customer;
import com.atachakki.components.staff.ShopStaff;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dues_And_refunds")
public class DueOrRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DueRefundType type;

    @Column(name = "amount", nullable = false, scale = 2)
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DueRefundStatus status;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_id", updatable = false, nullable = false)
    private ShopStaff addedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    private ShopStaff updatedBy;

    public DueOrRefund() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DueRefundType getType() {
        return type;
    }

    public void setType(DueRefundType type) {
        this.type = type;
    }

    public DueRefundStatus getStatus() {
        return status;
    }

    public void setStatus(DueRefundStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ShopStaff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(ShopStaff addedBy) {
        this.addedBy = addedBy;
    }

    public ShopStaff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(ShopStaff updatedBy) {
        this.updatedBy = updatedBy;
    }
}
