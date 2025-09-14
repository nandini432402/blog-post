package com.example.blogNest.repository;

import com.example.blogNest.model.entity.Comment;
import com.example.blogNest.model.entity.Blog;
import com.example.blogNest.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Comment entity operations
 * 
 * Provides data access methods for:
 * - Comment CRUD operations
 * - Hierarchical comment queries (replies and threads)
 * - Comment moderation
 * - Comment statistics and analytics
 * - User comment history
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Basic comment operations

    /**
     * Find comments by blog
     */
    Page<Comment> findByBlog(Blog blog, Pageable pageable);

    /**
     * Find comments by blog ID
     */
    Page<Comment> findByBlogId(Long blogId, Pageable pageable);

    /**
     * Find comments by author
     */
    Page<Comment> findByAuthor(User author, Pageable pageable);

    /**
     * Find comments by author ID
     */
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    // Hierarchical comment queries

    /**
     * Find top-level comments for a blog (no parent)
     */
    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.parent IS NULL AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt ASC")
    Page<Comment> findTopLevelCommentsByBlog(@Param("blogId") Long blogId, Pageable pageable);

    /**
     * Find replies to a specific comment
     */
    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt ASC")
    Page<Comment> findRepliesByParent(@Param("parentId") Long parentId, Pageable pageable);

    /**
     * Find all replies to a comment (recursive)
     */
    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId AND c.isDeleted = false ORDER BY c.createdAt ASC")
    List<Comment> findAllRepliesByParent(@Param("parentId") Long parentId);

    /**
     * Find comment thread (root comment and all its descendants)
     */
    @Query("WITH RECURSIVE comment_tree AS (" +
           "SELECT c.* FROM Comment c WHERE c.id = :rootId " +
           "UNION ALL " +
           "SELECT c.* FROM Comment c INNER JOIN comment_tree ct ON c.parent_id = ct.id" +
           ") SELECT * FROM comment_tree WHERE is_deleted = false AND is_approved = true ORDER BY created_at ASC")
    List<Comment> findCommentThread(@Param("rootId") Long rootId);

    /**
     * Count replies for a comment
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parent.id = :parentId AND c.isDeleted = false AND c.isApproved = true")
    long countRepliesByParent(@Param("parentId") Long parentId);

    // Moderation queries

    /**
     * Find comments awaiting approval
     */
    @Query("SELECT c FROM Comment c WHERE c.isApproved = false AND c.isDeleted = false ORDER BY c.createdAt ASC")
    Page<Comment> findCommentsAwaitingApproval(Pageable pageable);

    /**
     * Find flagged comments
     */
    @Query("SELECT c FROM Comment c WHERE c.isApproved = false AND c.isDeleted = false AND c.editReason IS NOT NULL ORDER BY c.createdAt ASC")
    Page<Comment> findFlaggedComments(Pageable pageable);

    /**
     * Find deleted comments (soft deleted)
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = true ORDER BY c.updatedAt DESC")
    Page<Comment> findDeletedComments(Pageable pageable);

    /**
     * Find comments by blog awaiting approval
     */
    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.isApproved = false AND c.isDeleted = false ORDER BY c.createdAt ASC")
    List<Comment> findUnapprovedCommentsByBlog(@Param("blogId") Long blogId);

    // Visibility and status queries

    /**
     * Find visible comments for a blog (approved and not deleted)
     */
    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt ASC")
    Page<Comment> findVisibleCommentsByBlog(@Param("blogId") Long blogId, Pageable pageable);

    /**
     * Find user's own comments for a blog (including unapproved)
     */
    @Query("SELECT c FROM Comment c WHERE c.blog.id = :blogId AND c.author.id = :userId AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findUserCommentsOnBlog(@Param("blogId") Long blogId, @Param("userId") Long userId);

    // Recent activity queries

    /**
     * Find recent comments
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false AND c.isApproved = true AND c.createdAt >= :since ORDER BY c.createdAt DESC")
    Page<Comment> findRecentComments(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find recent comments by user
     */
    @Query("SELECT c FROM Comment c WHERE c.author.id = :userId AND c.isDeleted = false AND c.createdAt >= :since ORDER BY c.createdAt DESC")
    Page<Comment> findRecentCommentsByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Find recent comments on user's blogs
     */
    @Query("SELECT c FROM Comment c WHERE c.blog.author.id = :authorId AND c.author.id != :authorId AND c.isDeleted = false AND c.isApproved = true AND c.createdAt >= :since ORDER BY c.createdAt DESC")
    Page<Comment> findRecentCommentsOnUserBlogs(@Param("authorId") Long authorId, @Param("since") LocalDateTime since, Pageable pageable);

    // Search and filter queries

    /**
     * Search comments by content
     */
    @Query("SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt DESC")
    Page<Comment> searchCommentsByContent(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find comments by date range
     */
    @Query("SELECT c FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt DESC")
    Page<Comment> findCommentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    /**
     * Find long comments (potentially spam)
     */
    @Query("SELECT c FROM Comment c WHERE LENGTH(c.content) > :minLength AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findLongComments(@Param("minLength") int minLength);

    // Statistics and analytics

    /**
     * Count comments by blog
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.blog.id = :blogId AND c.isDeleted = false AND c.isApproved = true")
    long countCommentsByBlog(@Param("blogId") Long blogId);

    /**
     * Count comments by user
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author.id = :userId AND c.isDeleted = false")
    long countCommentsByUser(@Param("userId") Long userId);

    /**
     * Find most liked comments
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false AND c.isApproved = true ORDER BY c.likesCount DESC")
    Page<Comment> findMostLikedComments(Pageable pageable);

    /**
     * Find most replied comments
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false AND c.isApproved = true ORDER BY c.repliesCount DESC")
    Page<Comment> findMostRepliedComments(Pageable pageable);

    /**
     * Find active commenters (users with most comments)
     */
    @Query("SELECT c.author, COUNT(c) as commentCount FROM Comment c WHERE c.isDeleted = false AND c.isApproved = true AND c.createdAt >= :since GROUP BY c.author ORDER BY COUNT(c) DESC")
    List<Object[]> findActiveCommenters(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Get comment statistics
     */
    @Query("SELECT new map(" +
           "COUNT(c) as totalComments, " +
           "SUM(CASE WHEN c.isApproved = true THEN 1 ELSE 0 END) as approvedComments, " +
           "SUM(CASE WHEN c.isDeleted = true THEN 1 ELSE 0 END) as deletedComments, " +
           "SUM(CASE WHEN c.parent IS NULL THEN 1 ELSE 0 END) as topLevelComments, " +
           "SUM(CASE WHEN c.parent IS NOT NULL THEN 1 ELSE 0 END) as replies, " +
           "SUM(c.likesCount) as totalLikes) " +
           "FROM Comment c")
    Object getCommentStatistics();

    /**
     * Get daily comment counts for date range
     */
    @Query("SELECT DATE(c.createdAt) as commentDate, COUNT(c) as commentCount " +
           "FROM Comment c WHERE c.createdAt BETWEEN :startDate AND :endDate AND c.isDeleted = false " +
           "GROUP BY DATE(c.createdAt) ORDER BY DATE(c.createdAt)")
    List<Object[]> getDailyCommentStats(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Update operations

    /**
     * Increment like count
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likesCount = c.likesCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * Decrement like count
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likesCount = GREATEST(0, c.likesCount - 1) WHERE c.id = :commentId")
    void decrementLikeCount(@Param("commentId") Long commentId);

    /**
     * Increment replies count
     */
    @Modifying
    @Query("UPDATE Comment c SET c.repliesCount = c.repliesCount + 1 WHERE c.id = :commentId")
    void incrementRepliesCount(@Param("commentId") Long commentId);

    /**
     * Decrement replies count
     */
    @Modifying
    @Query("UPDATE Comment c SET c.repliesCount = GREATEST(0, c.repliesCount - 1) WHERE c.id = :commentId")
    void decrementRepliesCount(@Param("commentId") Long commentId);

    /**
     * Approve comment
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isApproved = true WHERE c.id = :commentId")
    void approveComment(@Param("commentId") Long commentId);

    /**
     * Reject comment
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isApproved = false WHERE c.id = :commentId")
    void rejectComment(@Param("commentId") Long commentId);

    /**
     * Soft delete comment
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.content = '[Comment deleted]' WHERE c.id = :commentId")
    void softDeleteComment(@Param("commentId") Long commentId);

    /**
     * Restore deleted comment
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = false WHERE c.id = :commentId")
    void restoreComment(@Param("commentId") Long commentId);

    /**
     * Mark comment as edited
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isEdited = true, c.editReason = :reason WHERE c.id = :commentId")
    void markAsEdited(@Param("commentId") Long commentId, @Param("reason") String reason);

    /**
     * Bulk approve comments
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isApproved = true WHERE c.id IN :commentIds")
    void bulkApproveComments(@Param("commentIds") List<Long> commentIds);

    /**
     * Bulk delete comments
     */
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.content = '[Comment deleted]' WHERE c.id IN :commentIds")
    void bulkDeleteComments(@Param("commentIds") List<Long> commentIds);

    // Cleanup operations

    /**
     * Find old unapproved comments for cleanup
     */
    @Query("SELECT c FROM Comment c WHERE c.isApproved = false AND c.isDeleted = false AND c.createdAt < :cutoffDate")
    List<Comment> findOldUnapprovedComments(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find comments with no engagement for cleanup consideration
     */
    @Query("SELECT c FROM Comment c WHERE c.likesCount = 0 AND c.repliesCount = 0 AND c.createdAt < :cutoffDate AND c.isDeleted = false")
    List<Comment> findLowEngagementComments(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Count orphaned comments (comments whose blog was deleted)
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.blog IS NULL")
    long countOrphanedComments();

    // Custom queries for complex operations

    /**
     * Find conversation participants (users who commented on same blog)
     */
    @Query("SELECT DISTINCT c.author FROM Comment c WHERE c.blog.id = :blogId AND c.author.id != :excludeUserId AND c.isDeleted = false AND c.isApproved = true")
    List<User> findConversationParticipants(@Param("blogId") Long blogId, @Param("excludeUserId") Long excludeUserId);

    /**
     * Find users mentioned in comments (if @mention feature is implemented)
     */
    @Query("SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%@', :username, '%')) AND c.isDeleted = false AND c.isApproved = true ORDER BY c.createdAt DESC")
    List<Comment> findCommentsWithUserMention(@Param("username") String username);

    /**
     * Find trending comments (high engagement in recent period)
     */
    @Query("SELECT c FROM Comment c WHERE c.isDeleted = false AND c.isApproved = true AND c.createdAt >= :since " +
           "ORDER BY (c.likesCount * 0.6 + c.repliesCount * 0.4) DESC")
    Page<Comment> findTrendingComments(@Param("since") LocalDateTime since, Pageable pageable);
}