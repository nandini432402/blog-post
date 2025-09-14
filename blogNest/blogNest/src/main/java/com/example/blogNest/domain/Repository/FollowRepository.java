package com.example.blogNest.domain.Repository;

// Update the import below to match the actual location of the Follow class
// import com.example.blogNest.domain.model.entity.Follow;
// Update the import below to match the actual location of the Follow class
import com.example.blogNest.domain.model.entity.Follow;
// If the correct package is different, change it accordingly, e.g.:
// import com.example.blogNest.domain.entity.Follow;
import com.example.blogNest.domain.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Follow entity operations
 * 
 * Provides data access methods for:
 * - Follow/unfollow operations
 * - Follower and following queries
 * - Mutual follow detection
 * - Follow recommendations
 * - Follow statistics and analytics
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Basic follow operations

    /**
     * Find follow relationship between two users
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    /**
     * Find follow relationship by user IDs
     */
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Check if user1 follows user2
     */
    boolean existsByFollowerAndFollowing(User follower, User following);

    /**
     * Check if user1 follows user2 by IDs
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Delete follow relationship by user and following
     */
    void deleteByFollowerAndFollowing(User follower, User following);

    /**
     * Find all users followed by a specific user
     */
    Page<Follow> findAllByFollower(User follower, Pageable pageable);

    /**
     * Find all users who follow a specific user
     */
    Page<Follow> findAllByFollowing(User following, Pageable pageable);

    /**
     * Count how many followers a user has
     */
    long countByFollowing(User following);

    /**
     * Count how many users a user is following
     */
    long countByFollower(User follower);
}
