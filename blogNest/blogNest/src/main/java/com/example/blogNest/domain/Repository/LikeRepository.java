package com.example.blogNest.repository;

import com.example.blogNest.model.entity.Like;
import com.example.blogNest.model.entity.Blog;
import com.example.blogNest.model.entity.Comment;
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
 * Repository interface for Like entity operations
 * 
 * Provides data access methods for:
 * - Blog and comment like operations
 * - Like validation and checking
 * - User like history and preferences
 * - Like statistics and analytics
 * - Trending content based on likes
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Blog like operations

    /**
     * Find like by user and blog
     */
    Optional<Like> findByUserAndBlog(User user, Blog blog);

    /**
     * Find like by user ID and blog ID
     */
    Optional<Like> findByUserIdAndBlogId(Long userId, Long blogId);

    /**
     * Check if user liked a blog
     */
    boolean existsByUserAndBlog(User user, Blog blog);

    /**
     * Check if user liked a blog by IDs
     */
    boolean existsByUserIdAndBlogId(Long userId, Long blogId);

    /**
     * Find all likes for a blog
     */
    Page<Like> findByBlog(Blog blog, Pageable pageable);

    /**
     * Find all likes for a blog by ID
     */
    Page<Like> findByBlogId(Long blogId, Pageable pageable);

    /**
     * Count likes for a blog
     */
    long countByBlog(Blog blog);

    /**
     * Count likes for a blog by ID
     */
    long countByBlogId(Long blogId);

    // Comment like operations

    /**
     * Find like by user and comment
     */
    Optional<Like> findByUserAndComment(User user, Comment comment);

    /**
     * Find like by user ID and comment ID
     */
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

    /**
     * Check if user liked a comment
     */
    boolean existsByUserAndComment(User user, Comment comment);

    /**
     * Check if user liked a comment by IDs
     */
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    /**
     * Find all likes for a comment
     */
    Page<Like> findByComment(Comment comment, Pageable pageable);

    /**
     * Find all likes for a comment by ID
     */
    Page<Like> findByCommentId(Long commentId, Pageable pageable);

    /**
     * Count likes for a comment
     */
    long countByComment(Comment comment);

    /**
     * Count likes for a comment by ID
     */
    long countByCommentId(Long commentId);

    // User like history

    /**
     * Find all likes by user
     */
    Page<Like> findByUser(User user, Pageable pageable);

    /**
     * Find all likes by user ID
     */
    Page<Like> findByUserId(Long userId, Pageable pageable);

    /**
     * Find blog likes by user
     */
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.blog IS NOT NULL ORDER BY l.createdAt DESC")
    Page<Like> findBlogLikesByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find comment likes by user
     */
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.comment IS NOT NULL ORDER BY l.createdAt DESC")
    Page<Like> findCommentLikesByUser(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find recent likes by user
     */
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.createdAt >= :since ORDER BY l.createdAt DESC")
    Page<Like> findRecentLikesByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Count total likes by user
     */
    long countByUser(User user);

    /**
     * Count blog likes by user
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.user.id = :userId AND l.blog IS NOT NULL")
    long countBlogLikesByUser(@Param("userId") Long userId);

    /**
     * Count comment likes by user
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.user.id = :userId AND l.comment IS NOT NULL")
    long countCommentLikesByUser(@Param("userId") Long userId);

    // Author-centric queries

    /**
     * Find likes on user's blogs
     */
    @Query("SELECT l FROM Like l WHERE l.blog.author.id = :authorId AND l.blog IS NOT NULL ORDER BY l.createdAt DESC")
    Page<Like> findLikesOnUserBlogs(@Param("authorId") Long authorId, Pageable pageable);

    /**
     * Find likes on user's comments
     */
    @Query("SELECT l FROM Like l WHERE l.comment.author.id = :authorId AND l.comment IS NOT NULL ORDER BY l.createdAt DESC")
    Page<Like> findLikesOnUserComments(@Param("authorId") Long authorId, Pageable pageable);

    /**
     * Count likes received by user on their blogs
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.blog.author.id = :authorId AND l.blog IS NOT NULL")
    long countLikesReceivedOnBlogs(@Param("authorId") Long authorId);

    /**
     * Count likes received by user on their comments
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment.author.id = :authorId AND l.comment IS NOT NULL")
    long countLikesReceivedOnComments(@Param("authorId") Long authorId);

    /**
     * Find recent likes received by user
     */
    @Query("SELECT l FROM Like l WHERE (l.blog.author.id = :authorId OR l.comment.author.id = :authorId) " +
           "AND l.user.id != :authorId AND l.createdAt >= :since ORDER BY l.createdAt DESC")
    Page<Like> findRecentLikesReceived(@Param("authorId") Long authorId, @Param("since") LocalDateTime since, Pageable pageable);

    // Time-based queries

    /**
     * Find likes in date range
     */
    @Query("SELECT l FROM Like l WHERE l.createdAt BETWEEN :startDate AND :endDate ORDER BY l.createdAt DESC")
    Page<Like> findLikesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    /**
     * Find recent blog likes
     */
    @Query("SELECT l FROM Like l WHERE l.blog IS NOT NULL AND l.createdAt >= :since ORDER BY l.createdAt DESC")
    Page<Like> findRecentBlogLikes(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find recent comment likes
     */
    @Query("SELECT l FROM Like l WHERE l.comment IS NOT NULL AND l.createdAt >= :since ORDER BY l.createdAt DESC")
    Page<Like> findRecentCommentLikes(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Count likes today
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE DATE(l.createdAt) = CURRENT_DATE")
    long countLikesToday();

    // Analytics and statistics

    /**
     * Find most liked blogs in time period
     */
    @Query("SELECT l.blog, COUNT(l) as likeCount FROM Like l WHERE l.blog IS NOT NULL AND l.createdAt >= :since " +
           "GROUP BY l.blog ORDER BY COUNT(l) DESC")
    List<Object[]> findMostLikedBlogs(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find most liked comments in time period
     */
    @Query("SELECT l.comment, COUNT(l) as likeCount FROM Like l WHERE l.comment IS NOT NULL AND l.createdAt >= :since " +
           "GROUP BY l.comment ORDER BY COUNT(l) DESC")
    List<Object[]> findMostLikedComments(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find most active likers
     */
    @Query("SELECT l.user, COUNT(l) as likeCount FROM Like l WHERE l.createdAt >= :since " +
           "GROUP BY l.user ORDER BY COUNT(l) DESC")
    List<Object[]> findMostActiveLikers(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find users who received most likes
     */
    @Query("SELECT COALESCE(l.blog.author, l.comment.author) as author, COUNT(l) as likeCount FROM Like l " +
           "WHERE l.createdAt >= :since AND COALESCE(l.blog.author, l.comment.author) IS NOT NULL " +
           "GROUP BY COALESCE(l.blog.author, l.comment.author) ORDER BY COUNT(l) DESC")
    List<Object[]> findMostLikedAuthors(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Get like statistics
     */
    @Query("SELECT new map(" +
           "COUNT(l) as totalLikes, " +
           "SUM(CASE WHEN l.blog IS NOT NULL THEN 1 ELSE 0 END) as blogLikes, " +
           "SUM(CASE WHEN l.comment IS NOT NULL THEN 1 ELSE 0 END) as commentLikes, " +
           "COUNT(DISTINCT l.user) as uniqueLikers) " +
           "FROM Like l")
    Object getLikeStatistics();

    /**
     * Get daily like counts for date range
     */
    @Query("SELECT DATE(l.createdAt) as likeDate, COUNT(l) as likeCount " +
           "FROM Like l WHERE l.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(l.createdAt) ORDER BY DATE(l.createdAt)")
    List<Object[]> getDailyLikeStats(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Get like distribution by hour of day
     */
    @Query("SELECT HOUR(l.createdAt) as hour, COUNT(l) as likeCount " +
           "FROM Like l WHERE l.createdAt >= :since " +
           "GROUP BY HOUR(l.createdAt) ORDER BY HOUR(l.createdAt)")
    List<Object[]> getLikeDistributionByHour(@Param("since") LocalDateTime since);

    // User behavior analysis

    /**
     * Find user's favorite categories (based on liked blogs)
     */
    @Query("SELECT l.blog.category, COUNT(l) as likeCount FROM Like l " +
           "WHERE l.user.id = :userId AND l.blog IS NOT NULL AND l.blog.category IS NOT NULL " +
           "GROUP BY l.blog.category ORDER BY COUNT(l) DESC")
    List<Object[]> findUserFavoriteCategories(@Param("userId") Long userId);

    /**
     * Find users with similar taste (liked same blogs)
     */
    @Query("SELECT l2.user, COUNT(l2) as commonLikes FROM Like l1 JOIN Like l2 ON l1.blog = l2.blog " +
           "WHERE l1.user.id = :userId AND l2.user.id != :userId AND l1.blog IS NOT NULL " +
           "GROUP BY l2.user ORDER BY COUNT(l2) DESC")
    List<Object[]> findUsersWithSimilarTaste(@Param("userId") Long userId, Pageable pageable);

    /**
     * Check if users have mutual likes (both liked each other's content)
     */
    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE " +
           "((l.blog.author.id = :user1Id AND l.user.id = :user2Id) OR " +
           "(l.comment.author.id = :user1Id AND l.user.id = :user2Id)) AND " +
           "EXISTS(SELECT l2 FROM Like l2 WHERE " +
           "((l2.blog.author.id = :user2Id AND l2.user.id = :user1Id) OR " +
           "(l2.comment.author.id = :user2Id AND l2.user.id = :user1Id)))")
    boolean haveMutualLikes(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    // Cleanup and maintenance

    /**
     * Find orphaned likes (likes on deleted blogs/comments)
     */
    @Query("SELECT l FROM Like l WHERE (l.blog IS NOT NULL AND l.blog.status = 'ARCHIVED') OR " +
           "(l.comment IS NOT NULL AND l.comment.isDeleted = true)")
    List<Like> findOrphanedLikes();

    /**
     * Find duplicate likes (should not exist due to unique constraints, but for data integrity checks)
     */
    @Query("SELECT l1 FROM Like l1 JOIN Like l2 ON " +
           "((l1.blog = l2.blog AND l1.blog IS NOT NULL) OR (l1.comment = l2.comment AND l1.comment IS NOT NULL)) " +
           "AND l1.user = l2.user AND l1.id < l2.id")
    List<Like> findDuplicateLikes();

    // Bulk operations

    /**
     * Delete all likes for a blog
     */
    void deleteByBlogId(Long blogId);

    /**
     * Delete all likes for a comment
     */
    void deleteByCommentId(Long commentId);

    /**
     * Delete all likes by a user
     */
    void deleteByUserId(Long userId);

    /**
     * Count likes in the last N days
     */
    @Query("SELECT COUNT(l) FROM Like l WHERE l.createdAt >= :cutoffDate")
    long countRecentLikes(@Param("cutoffDate") LocalDateTime cutoffDate);
}