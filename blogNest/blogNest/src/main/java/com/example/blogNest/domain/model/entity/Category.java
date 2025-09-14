package com.example.blogNest.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity - Represents blog categories for content organization
 *
 * Features:
 * - Category name and description
 * - SEO-friendly slug
 * - Color coding for UI
 * - Blog count tracking
 * - Hierarchical structure support (parent-child categories)
 */
@Entity
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(columnNames = "slug")
})
public class Category extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 120)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Size(max = 7)
    @Column(name = "color")
    private String color;

    @Column(name = "icon")
    private String icon;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "blog_count", nullable = false)
    private Long blogCount = 0L;

    @Column(name = "sort_order")
    private Integer sortOrder;

    // Self-referencing relationship for parent-child categories
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> children = new ArrayList<>();

    // One-to-Many relationship with Blog
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Blog> blogs = new ArrayList<>();

    // Constructors
    public Category() {}

    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Category(String name, String slug, String description) {
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Long getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(Long blogCount) {
        this.blogCount = blogCount;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    // Utility methods

    /**
     * Check if this category has a parent
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Check if this category has children
     */
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    /**
     * Check if this is a root category (no parent)
     */
    public boolean isRootCategory() {
        return parent == null;
    }

    /**
     * Add a child category
     */
    public void addChild(Category child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Remove a child category
     */
    public void removeChild(Category child) {
        child.setParent(null);
        this.children.remove(child);
    }

    /**
     * Increment blog count
     */
    public void incrementBlogCount() {
        this.blogCount++;
        // Also increment parent's count if exists
        if (parent != null) {
            parent.incrementBlogCount();
        }
    }

    /**
     * Decrement blog count
     */
    public void decrementBlogCount() {
        this.blogCount = Math.max(0, this.blogCount - 1);
        // Also decrement parent's count if exists
        if (parent != null) {
            parent.decrementBlogCount();
        }
    }

    /**
     * Get full category path (for breadcrumbs)
     */
    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }

    /**
     * Get all ancestor categories
     */
    public List<Category> getAncestors() {
        List<Category> ancestors = new ArrayList<>();
        Category current = this.parent;

        while (current != null) {
            ancestors.add(0, current); // Add to beginning for correct order
            current = current.getParent();
        }

        return ancestors;
    }

    /**
     * Get all descendant categories (recursive)
     */
    public List<Category> getAllDescendants() {
        List<Category> descendants = new ArrayList<>();

        for (Category child : children) {
            descendants.add(child);
            descendants.addAll(child.getAllDescendants());
        }

        return descendants;
    }

    /**
     * Get depth level in hierarchy (root = 0)
     */
    public int getDepthLevel() {
        int depth = 0;
        Category current = this.parent;

        while (current != null) {
            depth++;
            current = current.getParent();
        }

        return depth;
    }

    /**
     * Get effective color (inherit from parent if not set)
     */
    public String getEffectiveColor() {
        if (color != null && !color.trim().isEmpty()) {
            return color;
        }

        if (parent != null) {
            return parent.getEffectiveColor();
        }

        return "#6B7280"; // Default gray color
    }

    /**
     * Get total blog count including all descendants
     */
    public Long getTotalBlogCount() {
        Long total = this.blogCount;

        for (Category child : children) {
            total += child.getTotalBlogCount();
        }

        return total;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Category category = (Category) obj;
        return id != null && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", isActive=" + isActive +
                ", blogCount=" + blogCount +
                ", hasParent=" + hasParent() +
                ", childrenCount=" + children.size() +
                '}';
    }
}