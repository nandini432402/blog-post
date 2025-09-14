package com.example.blogNest.model.enums;

/**
 * Notification Type Enumeration
 * 
 * Defines different types of notifications in the BlogNest platform:
 * - Social interactions (like, comment, follow)
 * - Blog-related notifications (new post, featured)
 * - System notifications (welcome, security)
 */
public enum NotificationType {
    
    // Social interaction notifications
    BLOG_LIKED("Blog Liked", "Someone liked your blog post", "👍", "#10B981", 3, true),
    BLOG_COMMENTED("Blog Commented", "Someone commented on your blog post", "💬", "#3B82F6", 4, true),
    COMMENT_REPLIED("Comment Replied", "Someone replied to your comment", "↩️", "#6366F1", 4, true),
    COMMENT_LIKED("Comment Liked", "Someone liked your comment", "❤️", "#EF4444", 2, false),
    
    // User interaction notifications
    USER_FOLLOWED("New Follower", "Someone started following you", "👥", "#8B5CF6", 3, true),
    USER_UNFOLLOWED("Unfollowed", "Someone unfollowed you", "👥", "#6B7280", 1, false),
    
    // Blog management notifications
    BLOG_PUBLISHED("Blog Published", "Your blog post has been published", "🚀", "#059669", 4, false),
    BLOG_FEATURED("Blog Featured", "Your blog post has been featured", "⭐", "#F59E0B", 5, true),
    BLOG_APPROVED("Blog Approved", "Your blog post has been approved", "✅", "#10B981", 3, true),
    BLOG_REJECTED("Blog Rejected", "Your blog post was rejected", "❌", "#EF4444", 4, true),
    
    // Comment management notifications
    COMMENT_APPROVED("Comment Approved", "Your comment has been approved", "✅", "#10B981", 2, false),
    COMMENT_REJECTED("Comment Rejected", "Your comment was rejected", "❌", "#EF4444", 3, true),
    COMMENT_FLAGGED("Comment Flagged", "Your comment has been flagged", "🚩", "#F97316", 4, true),
    
    // System notifications
    WELCOME("Welcome", "Welcome to BlogNest!", "🎉", "#8B5CF6", 3, true),
    ACCOUNT_VERIFIED("Account Verified", "Your account has been verified", "✅", "#10B981", 4, true),
    PASSWORD_CHANGED("Password Changed", "Your password has been changed", "🔒", "#F59E0B", 4, true),
    SECURITY_ALERT("Security Alert", "Security alert for your account", "🚨", "#EF4444", 5, true),
    
    // Admin notifications
    NEW_USER_REGISTERED("New User", "A new user has registered", "👤", "#3B82F6", 2, false),
    CONTENT_REPORTED("Content Reported", "Content has been reported", "🚩", "#F97316", 4, false),
    SYSTEM_MAINTENANCE("System Maintenance", "System maintenance notification", "⚙️", "#6B7280", 3, false),
    
    // Engagement notifications
    MILESTONE_REACHED("Milestone Reached", "You've reached a new milestone", "🎯", "#10B981", 4, true),
    WEEKLY_DIGEST("Weekly Digest", "Your weekly blog digest", "📊", "#3B82F6", 2, true),
    TRENDING_BLOG("Trending Blog", "Your blog is trending", "🔥", "#EF4444", 4, true);

    private final String displayName;
    private final String defaultMessage;
    private final String icon;
    private final String color;
    private final int priority;
    private final boolean requiresEmail;

    NotificationType(String displayName, String defaultMessage, String icon, 
                    String color, int priority, boolean requiresEmail) {
        this.displayName = displayName;
        this.defaultMessage = defaultMessage;
        this.icon = icon;
        this.color = color;
        this.priority = priority;
        this.requiresEmail = requiresEmail;
    }

    /**
     * Get the human-readable display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the default message for this notification type
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Get the icon for this notification type
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Get the color for this notification type
     */
    public String getColor() {
        return color;
    }

    /**
     * Get the priority level (1-5, higher is more important)
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Check if this notification type requires email notification
     */
    public boolean requiresEmail() {
        return requiresEmail;
    }

    /**
     * Check if this is a social interaction notification
     */
    public boolean isSocialInteraction() {
        return this == BLOG_LIKED || this == BLOG_COMMENTED || 
               this == COMMENT_REPLIED || this == COMMENT_LIKED ||
               this == USER_FOLLOWED || this == USER_UNFOLLOWED;
    }

    /**
     * Check if this is a blog-related notification
     */
    public boolean isBlogRelated() {
        return this == BLOG_LIKED || this == BLOG_COMMENTED || 
               this == BLOG_PUBLISHED || this == BLOG_FEATURED ||
               this == BLOG_APPROVED || this == BLOG_REJECTED ||
               this == TRENDING_BLOG;
    }

    /**
     * Check if this is a comment-related notification
     */
    public boolean isCommentRelated() {
        return this == BLOG_COMMENTED || this == COMMENT_REPLIED ||
               this == COMMENT_LIKED || this == COMMENT_APPROVED ||
               this == COMMENT_REJECTED || this == COMMENT_FLAGGED;
    }

    /**
     * Check if this is a system notification
     */
    public boolean isSystemNotification() {
        return this == WELCOME || this == ACCOUNT_VERIFIED ||
               this == PASSWORD_CHANGED || this == SECURITY_ALERT ||
               this == SYSTEM_MAINTENANCE || this == WEEKLY_DIGEST;
    }

    /**
     * Check if this is an admin notification
     */
    public boolean isAdminNotification() {
        return this == NEW_USER_REGISTERED || this == CONTENT_REPORTED ||
               this == SYSTEM_MAINTENANCE;
    }

    /**
     * Check if this is a high priority notification
     */
    public boolean isHighPriority() {
        return priority >= 4;
    }

    /**
     * Check if this is a low priority notification
     */
    public boolean isLowPriority() {
        return priority <= 2;
    }

    /**
     * Get notification types that require email
     */
    public static NotificationType[] getEmailNotificationTypes() {
        return java.util.Arrays.stream(values())
                .filter(NotificationType::requiresEmail)
                .toArray(NotificationType[]::new);
    }

    /**
     * Get social interaction notification types
     */
    public static NotificationType[] getSocialInteractionTypes() {
        return new NotificationType[]{
            BLOG_LIKED, BLOG_COMMENTED, COMMENT_REPLIED, 
            COMMENT_LIKED, USER_FOLLOWED, USER_UNFOLLOWED
        };
    }

    /**
     * Get system notification types
     */
    public static NotificationType[] getSystemNotificationTypes() {
        return new NotificationType[]{
            WELCOME, ACCOUNT_VERIFIED, PASSWORD_CHANGED, 
            SECURITY_ALERT, SYSTEM_MAINTENANCE, WEEKLY_DIGEST
        };
    }

    /**
     * Get admin notification types
     */
    public static NotificationType[] getAdminNotificationTypes() {
        return new NotificationType[]{
            NEW_USER_REGISTERED, CONTENT_REPORTED, SYSTEM_MAINTENANCE
        };
    }

    /**
     * Get notification type by name (case-insensitive)
     */
    public static NotificationType fromString(String type) {
        if (type == null) {
            return null;
        }
        
        try {
            return NotificationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get CSS class for styling based on priority
     */
    public String getCssClass() {
        return switch (priority) {
            case 1 -> "notification-low";
            case 2 -> "notification-normal";
            case 3 -> "notification-medium";
            case 4 -> "notification-high";
            case 5 -> "notification-critical";
            default -> "notification-normal";
        };
    }

    /**
     * Get description for UI tooltips
     */
    public String getDescription() {
        return switch (this) {
            case BLOG_LIKED -> "Notification sent when someone likes your blog post";
            case BLOG_COMMENTED -> "Notification sent when someone comments on your blog";
            case COMMENT_REPLIED -> "Notification sent when someone replies to your comment";
            case COMMENT_LIKED -> "Notification sent when someone likes your comment";
            case USER_FOLLOWED -> "Notification sent when someone follows you";
            case USER_UNFOLLOWED -> "Notification sent when someone unfollows you";
            case BLOG_PUBLISHED -> "Notification sent when your blog is published";
            case BLOG_FEATURED -> "Notification sent when your blog is featured";
            case BLOG_APPROVED -> "Notification sent when your blog is approved by moderators";
            case BLOG_REJECTED -> "Notification sent when your blog is rejected";
            case COMMENT_APPROVED -> "Notification sent when your comment is approved";
            case COMMENT_REJECTED -> "Notification sent when your comment is rejected";
            case COMMENT_FLAGGED -> "Notification sent when your comment is flagged";
            case WELCOME -> "Welcome notification for new users";
            case ACCOUNT_VERIFIED -> "Notification sent when account is verified";
            case PASSWORD_CHANGED -> "Security notification for password changes";
            case SECURITY_ALERT -> "Important security alerts";
            case NEW_USER_REGISTERED -> "Admin notification for new user registrations";
            case CONTENT_REPORTED -> "Admin notification for reported content";
            case SYSTEM_MAINTENANCE -> "System maintenance announcements";
            case MILESTONE_REACHED -> "Celebration notification for achievements";
            case WEEKLY_DIGEST -> "Weekly summary of activities";
            case TRENDING_BLOG -> "Notification when your blog is trending";
            default -> defaultMessage;
        };
    }

    /**
     * Check if notification can be disabled by user preferences
     */
    public boolean canBeDisabled() {
        // Critical security notifications cannot be disabled
        return this != SECURITY_ALERT && this != PASSWORD_CHANGED && this != ACCOUNT_VERIFIED;
    }

    @Override
    public String toString() {
        return displayName;
    }
}