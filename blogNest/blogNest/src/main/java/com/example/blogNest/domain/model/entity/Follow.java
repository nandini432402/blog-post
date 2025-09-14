package com.example.blogNest.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Follow Entity - Represents follower-following relationships between users
 * 
 * Features:
 * - User-to-User following relationships
 * - Prevents duplicate follows
 * - Tracks follow timestamp
 * - Supports mutual following detection
 * - Follow/unfollow notifications
 */
@Entity
@Table(name = "follows", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_follower_following", columnNames = {"follower_id", "following_id"})
    },
    indexes = {
        @Index(name = "idx_follow_follower", columnList = "follower_id"),
        @Index(name = "idx_follow_following", columnList = "following_id")
    }
)
public class Follow extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with User (the one who follows)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // Many-to-One relationship with User (the one being followed)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Column(name = "is_notification_sent", nullable = false)
    private Boolean isNotificationSent = false;

    // Constructors
    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public Boolean getIsNotificationSent() {
        return isNotificationSent;
    }

    public void setIsNotificationSent(Boolean notificationSent) {
        isNotificationSent = notificationSent;
    }

    // Utility methods

    /**
     * Get the username of the follower
     */
    public String getFollowerUsername() {
        return follower != null ? follower.getUsername() : "Unknown";
    }

    /**
     * Get the full name of the follower
     */
    public String getFollowerFullName() {
        return follower != null ? follower.getFullName() : "Unknown";
    }

    /**
     * Get the username of the user being followed
     */
    public String getFollowingUsername() {
        return following != null ? following.getUsername() : "Unknown";
    }

    /**
     * Get the full name of the user being followed
     */
    public String getFollowingFullName() {
        return following != null ? following.getFullName() : "Unknown";
    }

    /**
     * Check if this is a self-follow (user following themselves)
     * This should be prevented at the business logic level
     */
    public boolean isSelfFollow() {
        return follower != null && following != null && 
               follower.getId().equals(following.getId());
    }

    /**
     * Mark notification as sent
     */
    public void markNotificationAsSent() {
        this.isNotificationSent = true;
    }

    /**
     * Check if notification needs to be sent
     */
    public boolean needsNotification() {
        return !isNotificationSent;
    }

    /**
     * Get relationship description
     */
    public String getRelationshipDescription() {
        return getFollowerUsername() + " follows " + getFollowingUsername();
    }

    /**
     * Validate the follow relationship before persisting
     */
    @PrePersist
    @PreUpdate
    private void validateFollow() {
        if (follower == null || following == null) {
            throw new IllegalStateException("Both follower and following users must be specified");
        }
        
        if (isSelfFollow()) {
            throw new IllegalStateException("User cannot follow themselves");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Follow follow = (Follow) obj;
        
        if (id != null && follow.id != null) {
            return id.equals(follow.id);
        }
        
        // If IDs are null, compare by follower and following
        return follower != null && following != null &&
               follower.equals(follow.follower) && following.equals(follow.following);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        
        int result = follower != null ? follower.hashCode() : 0;
        result = 31 * result + (following != null ? following.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + getFollowerUsername() +
                ", following=" + getFollowingUsername() +
                ", isNotificationSent=" + isNotificationSent +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    /**
     * Create a new follow relationship
     */
    public static Follow create(User follower, User following) {
        if (follower == null || following == null) {
            throw new IllegalArgumentException("Both follower and following users must be provided");
        }
        
        if (follower.getId().equals(following.getId())) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }
        
        return new Follow(follower, following);
    }

    /**
     * Check if two users have a mutual follow relationship
     */
    public static boolean isMutualFollow(User user1, User user2) {
        if (user1 == null || user2 == null) {
            return false;
        }
        
        // This would typically be checked at the service level with repository queries
        // Here we're providing the logic structure
        boolean user1FollowsUser2 = user1.getFollowing().contains(user2);
        boolean user2FollowsUser1 = user2.getFollowing().contains(user1);
        
        return user1FollowsUser2 && user2FollowsUser1;
    }
}