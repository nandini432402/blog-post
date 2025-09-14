package com.example.blogNest.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Comment Entity - Represents comments on blog posts
 *
 * Features:
 * - Hierarchical comments (parent-child relationships for replies)
 * - Comment content with author information
 * - Like system for comments
 * - Soft delete functionality
 * - Moderation support
 * - Reply threading
 */
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comment_blog", columnList = "blog_id"),
        @Index(name = "idx_comment_author", columnList = "author_id"),
        @Index(name = "idx_comment_parent", columnList = "parent_id")
})
public class Comment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true;

    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @Column(name = "replies_count", nullable = false)
    private Long repliesCount = 0L;

    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited = false;

    @Size(max = 500)
    @Column(name = "edit_reason")
    private String editReason;

    // Many-to-One relationship with Blog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    // Many-to-One relationship with User (author)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Self-referencing relationship for replies
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> replies = new ArrayList<>();

    // One-to-Many relationship with Like
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    // Constructors
    public Comment() {}

    public Comment(String content, Blog blog, User author) {
        this.content = content;
        this.blog = blog;
        this.author = author;
    }

    public Comment(String content, Blog blog, User author, Comment parent) {
        this.content = content;
        this.blog = blog;
        this.author = author;
        this.parent = parent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean approved) {
        isApproved = approved;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Long getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Long repliesCount) {
        this.repliesCount = repliesCount;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(Boolean edited) {
        isEdited = edited;
    }

    public String getEditReason() {
        return editReason;
    }

    public void setEditReason(String editReason) {
        this.editReason = editReason;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    // Utility methods
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParent(this);
        this.repliesCount++;
    }

    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParent(null);
        this.repliesCount--;
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setComment(this);
        this.likesCount++;
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setComment(null);
        this.likesCount--;
    }

    public boolean isTopLevel() {
        return parent == null;
    }

    public boolean hasReplies() {
        return !replies.isEmpty();
    }

    public void softDelete() {
        this.isDeleted = true;
        this.content = "[Comment deleted]";
    }

    public void markAsEdited(String reason) {
        this.isEdited = true;
        this.editReason = reason;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override toString
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", isDeleted=" + isDeleted +
                ", isApproved=" + isApproved +
                ", likesCount=" + likesCount +
                ", repliesCount=" + repliesCount +
                ", isEdited=" + isEdited +
                ", authorId=" + (author != null ? author.getId() : null) +
                ", blogId=" + (blog != null ? blog.getId() : null) +
                ", parentId=" + (parent != null ? parent.getId() : null) +
                '}';
    }
}