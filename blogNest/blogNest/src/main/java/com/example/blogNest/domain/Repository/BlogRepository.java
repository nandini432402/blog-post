package com.example.blogNest.domain.Repository;

import com.example.blogNest.domain.model.entity.Blog;
import com.example.blogNest.domain.model.entity.User;
import com.example.blogNest.domain.model.entity.Category;
import com.example.blogNest.domain.model.enums.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Blog entity operations
 * 
 * Provides data access methods for:
 * - Blog CRUD operations
 * - Blog search and filtering
 * - Status and publication management
 * - Analytics and statistics
 * - Featured and trending blogs
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    // Basic blog operations

    /**
     * Find blog by slug
     */
    Optional<Blog> findBySlug(String slug);

    /**
     * Find blog by slug and status
     */
    Optional<Blog> findBySlugAndStatus(String slug, BlogStatus status);

    /**
     * Check if slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find blogs by author
     */
    Page<Blog> findByAuthor(User author, Pageable pageable);

    /**
     * Find blogs by author and status
     */
    Page<Blog> findByAuthorAndStatus(User author, BlogStatus status, Pageable pageable);

    /**
     * Find blogs by author ID
     */
    Page<Blog> findByAuthorId(Long authorId, Pageable pageable);

    // Status-based queries

    /**
     * Find all published blogs
     */
    Page<Blog> findByStatus(BlogStatus status, Pageable pageable);

    /**
     * Find published blogs ordered by publish date
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findPublishedBlogsOrderByPublishDate(Pageable pageable);

    /**
     * Find draft blogs by author
     */
    @Query("SELECT b FROM Blog b WHERE b.author.id = :authorId AND b.status = 'DRAFT' ORDER BY b.updatedAt DESC")
    Page<Blog> findDraftsByAuthor(@Param("authorId") Long authorId, Pageable pageable);

    /**
     * Find scheduled blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'SCHEDULED' AND b.scheduledAt <= :now ORDER BY b.scheduledAt ASC")
    List<Blog> findScheduledBlogsReadyToPublish(@Param("now") LocalDateTime now);

    /**
     * Find blogs awaiting approval (if moderation is enabled)
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'DRAFT' AND b.author.role = 'USER' ORDER BY b.createdAt ASC")
    Page<Blog> findBlogsAwaitingApproval(Pageable pageable);

    // Category and tag-based queries

    /**
     * Find blogs by category
     */
    Page<Blog> findByCategoryAndStatus(Category category, BlogStatus status, Pageable pageable);

    /**
     * Find blogs by category slug
     */
    @Query("SELECT b FROM Blog b WHERE b.category.slug = :categorySlug AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findByCategory(@Param("categorySlug") String categorySlug, Pageable pageable);

    /**
     * Find blogs by tag
     */
    @Query("SELECT DISTINCT b FROM Blog b JOIN b.blogTags bt WHERE bt.tag.slug = :tagSlug AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findByTag(@Param("tagSlug") String tagSlug, Pageable pageable);

    /**
     * Find blogs by multiple tags
     */
    @Query("SELECT DISTINCT b FROM Blog b JOIN b.blogTags bt WHERE bt.tag.slug IN :tagSlugs AND b.status = 'PUBLISHED' " +
           "GROUP BY b HAVING COUNT(DISTINCT bt.tag.slug) = :tagCount ORDER BY b.publishedAt DESC")
    Page<Blog> findByAllTags(@Param("tagSlugs") List<String> tagSlugs, @Param("tagCount") long tagCount, Pageable pageable);

    // Search operations

    /**
     * Search blogs by title
     */
    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    /**
     * Full-text search across title, summary, and content
     */
    @Query("SELECT b FROM Blog b WHERE " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.summary) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> searchBlogs(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Advanced search with filters
     */
    @Query("SELECT b FROM Blog b WHERE " +
           "(:searchTerm IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.summary) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:categoryId IS NULL OR b.category.id = :categoryId) " +
           "AND (:authorId IS NULL OR b.author.id = :authorId) " +
           "AND b.status = 'PUBLISHED' " +
           "ORDER BY b.publishedAt DESC")
    Page<Blog> advancedSearch(@Param("searchTerm") String searchTerm,
                             @Param("categoryId") Long categoryId,
                             @Param("authorId") Long authorId,
                             Pageable pageable);

    // Featured and trending blogs

    /**
     * Find featured blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.isFeatured = true AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findFeaturedBlogs(Pageable pageable);

    /**
     * Find trending blogs (high engagement in recent period)
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND b.publishedAt >= :since " +
           "ORDER BY (b.likesCount * 0.4 + b.commentsCount * 0.4 + b.viewsCount * 0.2) DESC")
    Page<Blog> findTrendingBlogs(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find popular blogs (most liked)
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' ORDER BY b.likesCount DESC")
    Page<Blog> findPopularBlogs(Pageable pageable);

    /**
     * Find most viewed blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' ORDER BY b.viewsCount DESC")
    Page<Blog> findMostViewedBlogs(Pageable pageable);

    /**
     * Find most commented blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' ORDER BY b.commentsCount DESC")
    Page<Blog> findMostCommentedBlogs(Pageable pageable);

    // Time-based queries

    /**
     * Find recent blogs
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND b.publishedAt >= :since ORDER BY b.publishedAt DESC")
    Page<Blog> findRecentBlogs(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find blogs by date range
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND b.publishedAt BETWEEN :startDate AND :endDate ORDER BY b.publishedAt DESC")
    Page<Blog> findBlogsByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);

    /**
     * Find blogs published today
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND DATE(b.publishedAt) = CURRENT_DATE ORDER BY b.publishedAt DESC")
    List<Blog> findBlogsPublishedToday();

    // User-specific queries

    /**
     * Find blogs from followed users
     */
    @Query("SELECT b FROM Blog b WHERE b.author.id IN " +
           "(SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) " +
           "AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findBlogsFromFollowedUsers(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find recommended blogs for user (based on liked categories and followed users)
     */
    @Query("SELECT DISTINCT b FROM Blog b WHERE b.status = 'PUBLISHED' AND " +
           "(b.category.id IN (SELECT DISTINCT b2.category.id FROM Blog b2 JOIN b2.likes l WHERE l.user.id = :userId) OR " +
           "b.author.id IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId)) " +
           "AND b.author.id != :userId ORDER BY b.publishedAt DESC")
    Page<Blog> findRecommendedBlogs(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find similar blogs (same category, excluding current blog)
     */
    @Query("SELECT b FROM Blog b WHERE b.category.id = :categoryId AND b.id != :blogId AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findSimilarBlogs(@Param("categoryId") Long categoryId, @Param("blogId") Long blogId, Pageable pageable);

    // Statistics and analytics

    /**
     * Count blogs by status
     */
    long countByStatus(BlogStatus status);

    /**
     * Count blogs by author
     */
    long countByAuthor(User author);

    /**
     * Count blogs by category
     */
    long countByCategoryAndStatus(Category category, BlogStatus status);

    /**
     * Get blog statistics
     */
    @Query("SELECT new map(" +
           "COUNT(b) as totalBlogs, " +
           "SUM(CASE WHEN b.status = 'PUBLISHED' THEN 1 ELSE 0 END) as publishedBlogs, " +
           "SUM(CASE WHEN b.status = 'DRAFT' THEN 1 ELSE 0 END) as draftBlogs, " +
           "SUM(CASE WHEN b.isFeatured = true THEN 1 ELSE 0 END) as featuredBlogs, " +
           "SUM(b.viewsCount) as totalViews, " +
           "SUM(b.likesCount) as totalLikes, " +
           "SUM(b.commentsCount) as totalComments) " +
           "FROM Blog b")
    Object getBlogStatistics();

    /**
     * Get daily blog statistics for date range
     */
    @Query("SELECT DATE(b.publishedAt) as publishDate, COUNT(b) as blogCount " +
           "FROM Blog b WHERE b.status = 'PUBLISHED' AND b.publishedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(b.publishedAt) ORDER BY DATE(b.publishedAt)")
    List<Object[]> getDailyBlogStats(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    // Update operations

    /**
     * Increment view count
     */
    @Modifying
    @Query("UPDATE Blog b SET b.viewsCount = b.viewsCount + 1 WHERE b.id = :blogId")
    void incrementViewCount(@Param("blogId") Long blogId);

    /**
     * Increment like count
     */
    @Modifying
    @Query("UPDATE Blog b SET b.likesCount = b.likesCount + 1 WHERE b.id = :blogId")
    void incrementLikeCount(@Param("blogId") Long blogId);

    /**
     * Decrement like count
     */
    @Modifying
    @Query("UPDATE Blog b SET b.likesCount = GREATEST(0, b.likesCount - 1) WHERE b.id = :blogId")
    void decrementLikeCount(@Param("blogId") Long blogId);

    /**
     * Increment comment count
     */
    @Modifying
    @Query("UPDATE Blog b SET b.commentsCount = b.commentsCount + 1 WHERE b.id = :blogId")
    void incrementCommentCount(@Param("blogId") Long blogId);

    /**
     * Decrement comment count
     */
    @Modifying
    @Query("UPDATE Blog b SET b.commentsCount = GREATEST(0, b.commentsCount - 1) WHERE b.id = :blogId")
    void decrementCommentCount(@Param("blogId") Long blogId);

    /**
     * Update blog status
     */
    @Modifying
    @Query("UPDATE Blog b SET b.status = :status, b.publishedAt = CASE WHEN :status = 'PUBLISHED' THEN CURRENT_TIMESTAMP ELSE b.publishedAt END WHERE b.id = :blogId")
    void updateBlogStatus(@Param("blogId") Long blogId, @Param("status") BlogStatus status);

    /**
     * Set blog as featured
     */
    @Modifying
    @Query("UPDATE Blog b SET b.isFeatured = :featured WHERE b.id = :blogId")
    void setBlogFeatured(@Param("blogId") Long blogId, @Param("featured") Boolean featured);

    /**
     * Publish scheduled blogs
     */
    @Modifying
    @Query("UPDATE Blog b SET b.status = 'PUBLISHED', b.publishedAt = CURRENT_TIMESTAMP WHERE b.status = 'SCHEDULED' AND b.scheduledAt <= :now")
    int publishScheduledBlogs(@Param("now") LocalDateTime now);

    // Cleanup operations

    /**
     * Find old draft blogs for cleanup
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'DRAFT' AND b.updatedAt < :cutoffDate")
    List<Blog> findOldDrafts(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find blogs with no interactions for cleanup consideration
     */
    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND b.likesCount = 0 AND b.commentsCount = 0 AND b.viewsCount < 10 AND b.publishedAt < :cutoffDate")
    List<Blog> findLowEngagementBlogs(@Param("cutoffDate") LocalDateTime cutoffDate);
}