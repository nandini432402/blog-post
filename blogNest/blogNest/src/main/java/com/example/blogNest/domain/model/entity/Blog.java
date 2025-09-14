package com.example.blogNest.domain.model.entity;

import com.example.blogNest.model.enums.BlogStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Blog Entity - Represents a blog post in the BlogNest platform
 *
 * Features:
 * - Blog content with title, summary, and full content
 * - SEO-friendly slug generation
 * - Status management (draft, published, archived)
 * - Featured image support
 * - Category and tags for organization
 * - Social features (likes, comments, views)
 * - Publishing and scheduling capabilities
 */
@Getter
@Entity
@Table(name = "blogs", indexes = {
        @Index(name = "idx_blog_status", columnList = "status"),
        @Index(name = "idx_blog_author", columnList = "author_id"),
        @Index(name = "idx_blog_category", columnList = "category_id"),
        @Index(name = "idx_blog_slug", columnList = "slug"),
        @Index(name = "idx_blog_published_at", columnList = "published_at")
})
public class Blog extends AuditableEntity {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Size(max = 250)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 500)
    @Column(name = "summary")
    private String summary;

    @NotBlank
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "featured_image_url")
    private String featuredImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BlogStatus status = BlogStatus.DRAFT;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Column(name = "is_comments_enabled", nullable = false)
    private Boolean isCommentsEnabled = true;

    @Column(name = "views_count", nullable = false)
    private Long viewsCount = 0L;

    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @Column(name = "comments_count", nullable = false)
    private Long commentsCount = 0L;

    @Column(name = "reading_time_minutes")
    private Integer readingTimeMinutes;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Size(max = 160)
    @Column(name = "meta_title")
    private String metaTitle;

    @Size(max = 320)
    @Column(name = "meta_description")
    private String metaDescription;

    @Size(max = 500)
    @Column(name = "meta_keywords")
    private String metaKeywords;

    // Many-to-One relationship with User (author)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Many-to-One relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // One-to-Many relationship with Comment
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    // One-to-Many relationship with Like
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    // Many-to-Many relationship with Tag through BlogTag
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BlogTag> blogTags = new ArrayList<>();

    // Constructors
    public Blog() {}

    public Blog(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.readingTimeMinutes = calculateReadingTime(content);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setContent(String content) {
        this.content = content;
        this.readingTimeMinutes = calculateReadingTime(content);
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }

    public void setStatus(BlogStatus status) {
        this.status = status;
        if (status == BlogStatus.PUBLISHED && publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    public void setIsFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public void setIsCommentsEnabled(Boolean commentsEnabled) {
        isCommentsEnabled = commentsEnabled;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setReadingTimeMinutes(Integer readingTimeMinutes) {
        this.readingTimeMinutes = readingTimeMinutes;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public void setBlogTags(List<BlogTag> blogTags) {
        this.blogTags = blogTags;
    }

    // Utility methods

    /**
     * Check if blog is published
     */
    public boolean isPublished() {
        return status == BlogStatus.PUBLISHED;
    }

    /**
     * Check if blog is draft
     */
    public boolean isDraft() {
        return status == BlogStatus.DRAFT;
    }

    /**
     * Check if blog is scheduled for future publishing
     */
    public boolean isScheduled() {
        return status == BlogStatus.SCHEDULED;
    }

    /**
     * Publish the blog
     */
    public void publish() {
        this.status = BlogStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.scheduledAt = null;
    }

    /**
     * Schedule the blog for future publishing
     */
    public void schedule(LocalDateTime scheduledTime) {
        this.status = BlogStatus.SCHEDULED;
        this.scheduledAt = scheduledTime;
    }

    /**
     * Archive the blog
     */
    public void archive() {
        this.status = BlogStatus.ARCHIVED;
    }

    /**
     * Move blog to draft
     */
    public void makeDraft() {
        this.status = BlogStatus.DRAFT;
        this.publishedAt = null;
        this.scheduledAt = null;
    }

    /**
     * Increment view count
     */
    public void incrementViewCount() {
        this.viewsCount++;
    }

    /**
     * Increment like count
     */
    public void incrementLikeCount() {
        this.likesCount++;
    }

    /**
     * Decrement like count
     */
    public void decrementLikeCount() {
        this.likesCount = Math.max(0, this.likesCount - 1);
    }

    /**
     * Increment comment count
     */
    public void incrementCommentCount() {
        this.commentsCount++;
    }

    /**
     * Decrement comment count
     */
    public void decrementCommentCount() {
        this.commentsCount = Math.max(0, this.commentsCount - 1);
    }

    /**
     * Calculate reading time based on content length
     * Assumes average reading speed of 200 words per minute
     */
    private Integer calculateReadingTime(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 1;
        }

        // Remove HTML tags and count words
        String plainText = content.replaceAll("<[^>]+>", " ");
        String[] words = plainText.trim().split("\\s+");
        int wordCount = words.length;

        // Calculate reading time (minimum 1 minute)
        int readingTime = Math.max(1, Math.round((float) wordCount / 200));
        return readingTime;
    }

    /**
     * Get summary from content if summary is not set
     */
    public String getEffectiveSummary() {
        if (summary != null && !summary.trim().isEmpty()) {
            return summary;
        }

        // Generate summary from first 150 characters of content
        if (content != null && content.length() > 150) {
            String plainText = content.replaceAll("<[^>]+>", " ");
            return plainText.substring(0, 150) + "...";
        }

        return content != null ? content : "";
    }

    /**
     * Get effective meta title
     */
    public String getEffectiveMetaTitle() {
        return metaTitle != null && !metaTitle.trim().isEmpty() ? metaTitle : title;
    }

    /**
     * Get effective meta description
     */
    public String getEffectiveMetaDescription() {
        return metaDescription != null && !metaDescription.trim().isEmpty() ?
                metaDescription : getEffectiveSummary();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Blog blog = (Blog) obj;
        return id != null && id.equals(blog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", status=" + status +
                ", viewsCount=" + viewsCount +
                ", likesCount=" + likesCount +
                ", commentsCount=" + commentsCount +
                ", publishedAt=" + publishedAt +
                '}';
    }
}