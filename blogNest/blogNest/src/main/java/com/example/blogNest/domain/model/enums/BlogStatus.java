package com.example.blogNest.domain.model.enums;

/**
 * Blog Status Enumeration
 *
 * Defines the different states a blog post can have:
 * - DRAFT: Blog is being written, not visible to public
 * - PUBLISHED: Blog is live and visible to public
 * - SCHEDULED: Blog is scheduled for future publishing
 * - ARCHIVED: Blog is archived and not visible to public
 */
public enum BlogStatus {

    /**
     * Draft status - Blog is being written
     * - Not visible to public
     * - Only author can see and edit
     * - Can be published or scheduled
     */
    DRAFT("Draft", "Blog is being written and is not visible to public"),

    /**
     * Published status - Blog is live
     * - Visible to all users
     * - Can receive likes and comments
     * - Appears in search results and feeds
     */
    PUBLISHED("Published", "Blog is live and visible to all users"),

    /**
     * Scheduled status - Blog is scheduled for future publishing
     * - Not visible to public yet
     * - Will be automatically published at scheduled time
     * - Can be edited before publish time
     */
    SCHEDULED("Scheduled", "Blog is scheduled for future publishing"),

    /**
     * Archived status - Blog is archived
     * - Not visible to public
     * - Preserved for author reference
     * - Can be republished if needed
     */
    ARCHIVED("Archived", "Blog is archived and not visible to public");

    private final String displayName;
    private final String description;

    BlogStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the human-readable display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of this status
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if blog with this status is visible to public
     */
    public boolean isPubliclyVisible() {
        return this == PUBLISHED;
    }

    /**
     * Check if blog with this status can be edited
     */
    public boolean canBeEdited() {
        return this == DRAFT || this == SCHEDULED;
    }

    /**
     * Check if blog with this status can receive interactions (likes, comments)
     */
    public boolean canReceiveInteractions() {
        return this == PUBLISHED;
    }

    /**
     * Check if blog with this status appears in search results
     */
    public boolean appearsInSearch() {
        return this == PUBLISHED;
    }

    /**
     * Get status by name (case-insensitive)
     */
    public static BlogStatus fromString(String status) {
        if (status == null) {
            return DRAFT;
        }

        try {
            return BlogStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DRAFT; // Default to draft if invalid status
        }
    }

    /**
     * Get all statuses that are visible to public
     */
    public static BlogStatus[] getPublicStatuses() {
        return new BlogStatus[]{PUBLISHED};
    }

    /**
     * Get all statuses that can be edited
     */
    public static BlogStatus[] getEditableStatuses() {
        return new BlogStatus[]{DRAFT, SCHEDULED};
    }

    @Override
    public String toString() {
        return displayName;
    }
}