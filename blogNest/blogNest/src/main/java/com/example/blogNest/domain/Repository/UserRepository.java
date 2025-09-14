package com.example.blogNest.repository;

import com.example.blogNest.model.entity.Follow;
import com.example.blogNest.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
     * Delete follow relationship
     */
    void deleteByFollowerI