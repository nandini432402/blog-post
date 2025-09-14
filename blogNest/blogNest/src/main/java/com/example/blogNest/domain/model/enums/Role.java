package com.example.blogNest.domain.model.enums;

/**
 * User Role Enumeration
 *
 * Defines different roles a user can have in the BlogNest platform:
 * - USER: Regular user who can create blogs, comment, like, follow
 * - MODERATOR: Can moderate content, manage comments, handle reports
 * - ADMIN: Full access to all features including user management
 */
public enum Role {

    /**
     * Regular user role
     * Permissions:
     * - Create, edit, delete own blogs
     * - Comment on blogs
     * - Like/unlike blogs and comments
     * - Follow/unfollow other users
     * - Manage own profile
     */
    USER("ROLE_USER", "User"),

    /**
     * Moderator role
     * Permissions:
     * - All USER permissions
     * - Delete inappropriate comments
     * - Hide/unhide blogs
     * - Manage categories and tags
     * - View moderation dashboard
     */
    MODERATOR("ROLE_MODERATOR", "Moderator"),

    /**
     * Administrator role
     * Permissions:
     * - All MODERATOR permissions
     * - Manage user accounts
     * - Delete any content
     * - Access system analytics
     * - Configure system settings
     */
    ADMIN("ROLE_ADMIN", "Administrator");

    private final String authority;
    private final String displayName;

    Role(String authority, String displayName) {
        this.authority = authority;
        this.displayName = displayName;
    }

    /**
     * Get the Spring Security authority string
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Get the human-readable display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if this role has at least the same level as the given role
     */
    public boolean hasAuthorityLevel(Role role) {
        return this.ordinal() >= role.ordinal();
    }

    /**
     * Check if this role can manage users
     */
    public boolean canManageUsers() {
        return this == ADMIN;
    }

    /**
     * Check if this role can moderate content
     */
    public boolean canModerateContent() {
        return this == ADMIN || this == MODERATOR;
    }

    /**
     * Check if this role can access admin features
     */
    public boolean canAccessAdminFeatures() {
        return this == ADMIN;
    }

    /**
     * Get role by authority string
     */
    public static Role fromAuthority(String authority) {
        for (Role role : values()) {
            if (role.getAuthority().equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }

    @Override
    public String toString() {
        return displayName;
    }
}