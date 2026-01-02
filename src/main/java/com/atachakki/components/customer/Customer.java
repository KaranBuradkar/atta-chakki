package com.atachakki.components.customer;

import com.atachakki.components.shop.Shop;
import com.atachakki.components.staff.ShopStaff;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers",
indexes = {
        @Index(name = "idx_shop_deleted", columnList = "shop_id, deleted"),
        @Index(name = "idx_shop_name_deleted", columnList = "shop_id, name, deleted"),
        @Index(name = "idx_name_deleted", columnList = "name, deleted"),
        @Index(name = "idx_id_shop", columnList = "id, shop_id", unique = true)
})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "email")
    private String email;

    @Column(name = "block", nullable = false)
    private Boolean block;

    @Column(name = "specification")
    private String specification;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

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
    @JoinColumn(name = "updated_by_id", nullable = false)
    private ShopStaff updatedBy;

    public Customer() {}

    public Customer(
            String name, Shop shop,
            String email, Boolean block,
            String specification
    ) {
        this.name = name;
        this.shop = shop;
        this.email = email;
        this.block = block;
        this.specification = specification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecification() {
        return specification;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public ShopStaff getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(ShopStaff updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ShopStaff getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(ShopStaff addedBy) {
        this.addedBy = addedBy;
        setUpdatedBy(addedBy);
    }
}