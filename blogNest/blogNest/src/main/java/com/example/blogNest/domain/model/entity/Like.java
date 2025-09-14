package com.example.blogNest.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Like Entity - Represents likes on blogs and comments
 * 
 * Features:
 * - Users can like blogs and comments
 * - Prevents duplicate likes from same user
 * - Tracks like timestamp
 * - Supports both blog likes and comment likes
 */
@Entity
@Table(name = "likes", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_blog_like", columnNames = {"user_id", "blog_id"}),
        @UniqueConstraint(name = "uk_user_comment_like", columnNames = {"user_id", "comment_id"})
    },
    indexes = {
        @Index(name = "idx_like_user", columnList = "user_id"),
        @Index(name = "idx_like_blog", columnList = "blog_id"),
        @Index(name = "idx_like_comment", columnList = "comment_id")
    }
)
public class Like extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with User
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-One relationship with Blog (optional - either blog or comment)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    // Many-to-One relationship with Comment (optional - either blog or comment)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // Constructors
    public Like() {}

    /**
     * Constructor for blog like
     */
    public Like(User user, Blog blog) {
        this.user = user;
        this.blog = blog;
    }

    /**
     * Constructor for comment like
     */
    public Like(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    // Utility methods

    /**
     * Check if this is a blog like
     */
    public boolean isBlogLike() {
        return blog != null && comment == null;
    }

    /**
     * Check if this is a comment like
     */
    public boolean isCommentLike() {
        return comment != null && blog == null;
    }

    /**
     * Get the target type (BLOG or COMMENT)
     */
    public LikeType getType() {
        if (isBlogLike()) {
            return LikeType.BLOG;
        } else if (isCommentLike()) {
            return LikeType.COMMENT;
        } else {
            return LikeType.UNKNOWN;
        }
    }

    /**
     * Get the target ID (blog ID or comment ID)
     */
    public Long getTargetId() {
        if (isBlogLike()) {
            return blog.getId();
        } else if (isCommentLike()) {
            return comment.getId();
        }
        return null;
    }

    /**
     * Get the target title/content preview
     */
    public String getTargetTitle() {
        if (isBlogLike()) {
            return blog.getTitle();
        } else if (isCommentLike()) {
            String content = comment.getContent();
            return content.length() > 50 ? content.substring(0, 50) + "..." : content;
        }
        return "Unknown";
    }

    /**
     * Get the username of the liker
     */
    public String getUserUsername() {
        return user != null ? user.getUsername() : "Unknown";
    }

    /**
     * Get the full name of the liker
     */
    public String getUserFullName() {
        return user != null ? user.getFullName() : "Unknown";
    }

    /**
     * Validate that either blog or comment is set, but not both
     */
    @PrePersist
    @PreUpdate
    private void validateLikeTarget() {
        if (blog == null && comment == null) {
            throw new IllegalStateException("Like must be associated with either a blog or a comment");
        }
        
        if (blog != null && comment != null) {
            throw new IllegalStateException("Like cannot be associated with both a blog and a comment");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Like like = (Like) obj;
        
        if (id != null && like.id != null) {
            return id.equals(like.id);
        }
        
        // If IDs are null, compare by user and target
        boolean userEquals = user != null && user.equals(like.user);
        boolean targetEquals = false;
        
        if (isBlogLike() && like.isBlogLike()) {
            targetEquals = blog.equals(like.blog);
        } else if (isCommentLike() && like.isCommentLike()) {
            targetEquals = comment.equals(like.comment);
        }
        
        return userEquals && targetEquals;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        
        int result = user != null ? user.hashCode() : 0;
        
        if (isBlogLike()) {
            result = 31 * result + blog.hashCode();
        } else if (isCommentLike()) {
            result = 31 * result + comment.hashCode();
        }
        
        return result;
    }

    @Override
    public String toString() {
        String target = "unknown";
        if (isBlogLike()) {
            target = "blog:" + blog.getId();
        } else if (isCommentLike()) {
            target = "comment:" + comment.getId();
        }
        
        return "Like{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", target=" + target +
                ", type=" + getType() +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    /**
     * Enumeration for like types
     */
    public enum LikeType {
        BLOG("Blog Like"),
        COMMENT("Comment Like"),
        UNKNOWN("Unknown");

        private final String displayName;

        LikeType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}