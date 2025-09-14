package com.example.blogNest.domain.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag Entity - Represents tags for blog content organization
 *
 * Features:
 * - Tag name and description
 * - SEO-friendly slug
 * - Usage count tracking
 * - Color coding for UI
 * - Many-to-Many relationship with blogs through BlogTag
 */
@Entity
@Table(name = "tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = "slug")
})
public class Tag extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 60)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 200)
    @Column(name = "description")
    private String description;

    @Size(max = 7)
    @Column(name = "color")
    private String color;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "usage_count", nullable = false)
    private Long usageCount = 0L;

    // One-to-Many relationship with BlogTag
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BlogTag> blogTags = new ArrayList<>();

    // Constructors
    public Tag() {}

    public Tag(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Tag(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    // Getters and Setters
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }

    public List<BlogTag> getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(List<BlogTag> blogTags) {
        this.blogTags = blogTags;
    }

    // Utility methods

    /**
     * Increment usage count when tag is used in a blog
     */
    public void incrementUsageCount() {
        this.usageCount++;
    }

    /**
     * Decrement usage count when tag is removed from a blog
     */
    public void decrementUsageCount() {
        this.usageCount = Math.max(0, this.usageCount - 1);
    }

    /**
     * Check if tag is popular (used in many blogs)
     * Consider a tag popular if used in more than 10 blogs
     */
    public boolean isPopular() {
        return usageCount > 10;
    }

    /**
     * Get effective color (use default if not set)
     */
    public String getEffectiveColor() {
        if (color != null && !color.trim().isEmpty()) {
            return color;
        }
        return "#3B82F6"; // Default blue color
    }

    /**
     * Get popularity level based on usage count
     */
    public TagPopularity getPopularityLevel() {
        if (usageCount == 0) {
            return TagPopularity.UNUSED;
        } else if (usageCount <= 5) {
            return TagPopularity.LOW;
        } else if (usageCount <= 20) {
            return TagPopularity.MEDIUM;
        } else if (usageCount <= 50) {
            return TagPopularity.HIGH;
        } else {
            return TagPopularity.TRENDING;
        }
    }

    /**
     * Get display name with usage count
     */
    public String getDisplayNameWithCount() {
        return name + " (" + usageCount + ")";
    }

    /**
     * Check if tag can be safely deleted (not used in any blog)
     */
    public boolean canBeDeleted() {
        return usageCount == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tag tag = (Tag) obj;
        return id != null && id.equals(tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", isActive=" + isActive +
                ", usageCount=" + usageCount +
                '}';
    }

    /**
     * Enum for tag popularity levels
     */
    public enum TagPopularity {
        UNUSED("Unused", "#6B7280"),
        LOW("Low", "#F59E0B"),
        MEDIUM("Medium", "#3B82F6"),
        HIGH("High", "#10B981"),
        TRENDING("Trending", "#EF4444");

        private final String displayName;
        private final String color;

        TagPopularity(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColor() {
            return color;
        }
    }
}