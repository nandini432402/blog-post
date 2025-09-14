package com.example.blogNest.domain.model.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base class for all auditable entities in BlogNest application
 *
 * Provides common audit fields that are automatically populated:
 * - createdAt: When the entity was created
 * - updatedAt: When the entity was last modified
 * - createdBy: Username of the user who created the entity
 * - modifiedBy: Username of the user who last modified the entity
 *
 * All entities that need audit tracking should extend this class
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    /**
     * Timestamp when the entity was created
     * Automatically populated by Spring Data JPA Auditing
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated
     * Automatically updated by Spring Data JPA Auditing
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Username of the user who created this entity
     * Automatically populated by Spring Data JPA Auditing using Spring Security context
     */
    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    /**
     * Username of the user who last modified this entity
     * Automatically updated by Spring Data JPA Auditing using Spring Security context
     */
    @LastModifiedBy
    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    /**
     * Version field for optimistic locking
     * Prevents concurrent modification conflicts
     */
    @Version
    @Column(name = "version")
    private Long version;

    // Default constructor
    protected AuditableEntity() {
        // Protected constructor for JPA
    }

    // Getters and Setters

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Lifecycle callback method called before persisting the entity
     * Can be overridden by subclasses for custom logic
     */
    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Lifecycle callback method called before updating the entity
     * Can be overridden by subclasses for custom logic
     */
    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Utility method to check if this entity is new (not yet persisted)
     */
    public boolean isNew() {
        return this.createdAt == null;
    }

    /**
     * Utility method to get a formatted creation timestamp
     */
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toString() : "";
    }

    /**
     * Utility method to get a formatted update timestamp
     */
    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toString() : "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AuditableEntity that = (AuditableEntity) obj;

        if (createdAt != null && that.createdAt != null) {
            return createdAt.equals(that.createdAt) &&
                    (createdBy != null ? createdBy.equals(that.createdBy) : that.createdBy == null);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", version=" + version +
                '}';
    }
}