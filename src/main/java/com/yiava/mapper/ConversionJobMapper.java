package com.yiava.mapper;

import com.yiava.entity.ConversionJob;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MyBatis mapper for ConversionJob entity
 */
public interface ConversionJobMapper {

    @Insert("INSERT INTO conversion_job (document_id, status, progress_percentage, current_page, total_pages, started_at) " +
            "VALUES (#{documentId}, #{status}, #{progressPercentage}, #{currentPage}, #{totalPages}, #{startedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ConversionJob job);

    @Update("UPDATE conversion_job SET status = #{status}, progress_percentage = #{progressPercentage}, " +
            "current_page = #{currentPage} WHERE id = #{id}")
    int updateProgress(@Param("id") Long id,
                       @Param("status") String status,
                       @Param("progressPercentage") Integer progressPercentage,
                       @Param("currentPage") Integer currentPage);

    @Update("UPDATE conversion_job SET status = #{status}, completed_at = #{completedAt} " +
            "WHERE id = #{id}")
    int updateCompletion(@Param("id") Long id,
                        @Param("status") String status,
                        @Param("completedAt") LocalDateTime completedAt);

    @Update("UPDATE conversion_job SET status = #{status}, error_message = #{errorMessage} " +
            "WHERE id = #{id}")
    int updateFailure(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("errorMessage") String errorMessage);

    @Select("SELECT * FROM conversion_job WHERE id = #{id}")
    ConversionJob selectById(Long id);

    @Select("SELECT * FROM conversion_job WHERE document_id = #{documentId}")
    ConversionJob selectByDocumentId(Long documentId);

    @Select("SELECT * FROM conversion_job WHERE status = #{status} ORDER BY started_at DESC")
    List<ConversionJob> selectByStatus(String status);

    @Select("SELECT * FROM conversion_job WHERE started_at >= #{startTime} ORDER BY started_at DESC")
    List<ConversionJob> selectSince(LocalDateTime startTime);

    @Delete("DELETE FROM conversion_job WHERE id = #{id}")
    int deleteById(Long id);
}
