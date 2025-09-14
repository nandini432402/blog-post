package com.example.blogNest.model.entity;

import com.example.blogNest.model.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Notification Entity - Represents user notifications
 * 
 * Features:
 * - Different types of notifications (like, comment, follow, etc.)
 * - Read/unread status tracking
 * - Reference to related entities (blog, comment, user)
 * - Customizable message and action URL
 * - Bulk operations support
 */
@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_recipient", columnList = "recipient_id"),
    @Index(name = "idx_notification_type", columnList = "type"),
    @Index(name = "idx_notification_read", columnList = "is_read"),
    @Index(name = "idx_notification_created", columnList = "created_at")
})
public class Notification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Size(max = 500)
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "is_email_sent", nullable = false)
    private Boolean isEmailSent = false;

    // References to related entities
    @Column(name = "related_blog_id")
    private Long relatedBlogId;

    @Column(name = "related_comment_id")
    private Long relatedCommentId;

    @Column(name = "related_user_id")
    private Long relatedUserId;

    // Many-to-One relationship with User (recipient)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    // Many-to-One relationship with User (actor - who performed the action)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor;

    // Constructors
    public Notification() {}

    public Notification(NotificationType type, String title, String message, User recipient) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.recipient = recipient;
    }

    public Notification(NotificationType type, String title, String message, User recipient, User actor) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.recipient = recipient;
        this.actor = actor;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public Boolean getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(Boolean emailSent) {
        isEmailSent = emailSent;
    }

    public Long getRelatedBlogId() {
        return relatedBlogId;
    }

    public void setRelatedBlogId(Long relatedBlogId) {
        this.relatedBlogId = relatedBlogId;
    }

    public Long getRelatedCommentId() {
        return relatedCommentId;
    }

    public void setRelatedCommentId(Long relatedCommentId) {
        this.relatedCommentId = relatedCommentId;
    }

    public Long getRelatedUserId() {
        return relatedUserId;
    }

    public void setRelatedUserId(Long relatedUserId) {
        this.relatedUserId = relatedUserId;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    // Utility methods

    /**
     * Mark notification as read
     */
    public void markAsRead() {
        this.isRead = true;
    }

    /**
     * Mark notification as unread
     */
    public void markAsUnread() {
        this.isRead = false;
    }

    /**
     * Mark email as sent
     */
    public void markEmailAsSent() {
        this.isEmailSent = true;
    }

    /**
     * Check if notification is unread
     */
    public boolean isUnread() {
        return !isRead;
    }

    /**
     * Check if notification requires email
     */
    public boolean requiresEmail() {
        return type.requiresEmail() && !isEmailSent;
    }

    /**
     * Get recipient username
     */
    public String getRecipientUsername() {
        return recipient != null ? recipient.getUsername() : "Unknown";
    }

    /**
     * Get actor username
     */
    public String getActorUsername() {
        return actor != null ? actor.getUsername() : "System";
    }

    /**
     * Get actor full name
     */
    public String getActorFullName() {
        return actor != null ? actor.getFullName() : "System";
    }

    /**
     * Check if this notification has an actor
     */
    public boolean hasActor() {
        return actor != null;
    }

    /**
     * Get notification icon based on type
     */
    public String getIcon() {
        return type.getIcon();
    }

    /**
     * Get notification color based on type
     */
    public String getColor() {
        return type.getColor();
    }

    /**
     * Get priority level based on type
     */
    public int getPriority() {
        return type.getPriority();
    }

    /**
     * Check if notification is related to a blog
     */
    public boolean isRelatedToBlog() {
        return relatedBlogId != null;
    }

    /**
     * Check if notification is related to a comment
     */
    public boolean isRelatedToComment() {
        return relatedCommentId != null;
    }

    /**
     * Check if notification is related to a user
     */
    public boolean isRelatedToUser() {
        return relatedUserId != null;
    }

    /**
     * Create a blog-related notification
     */
    public static Notification forBlog(NotificationType type, String title, String message, 
                                      User recipient, User actor, Long blogId, String actionUrl) {
        Notification notification = new Notification(type, title, message, recipient, actor);
        notification.setRelatedBlogId(blogId);
        notification.setActionUrl(actionUrl);
        return notification;
    }

    /**
     * Create a comment-related notification
     */
    public static Notification forComment(NotificationType type, String title, String message,
                                         User recipient, User actor, Long commentId, String actionUrl) {
        Notification notification = new Notification(type, title, message, recipient, actor);
        notification.setRelatedCommentId(commentId);
        notification.setActionUrl(actionUrl);
        return notification;
    }

    /**
     * Create a user-related notification (follow, etc.)
     */
    public static Notification forUser(NotificationType type, String title, String message,
                                      User recipient, User actor, String actionUrl) {
        Notification notification = new Notification(type, title, message, recipient, actor);
        notification.setRelatedUserId(actor.getId());
        notification.setActionUrl(actionUrl);
        return notification;
    }

    /**
     * Create a system notification
     */
    public static Notification systemNotification(NotificationType type, String title, 
                                                 String message, User recipient, String actionUrl) {
        Notification notification = new Notification(type, title, message, recipient);
        notification.setActionUrl(actionUrl);
        return notification;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Notification that = (Notification) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", recipient=" + getRecipientUsername() +
                ", actor=" + getActorUsername() +
                ", isRead=" + isRead +
                ", isEmailSent=" + isEmailSent +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}