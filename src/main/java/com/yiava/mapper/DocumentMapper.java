package com.yiava.mapper;

import com.yiava.entity.Document;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MyBatis mapper for Document entity
 */
public interface DocumentMapper {

    @Insert("INSERT INTO document (original_filename, file_extension, file_size, storage_path, status) " +
            "VALUES (#{originalFilename}, #{fileExtension}, #{fileSize}, #{storagePath}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Document document);

    @Update("UPDATE document SET page_count = #{pageCount}, status = #{status} " +
            "WHERE id = #{id}")
    int updatePageCountAndStatus(@Param("id") Long id,
                                 @Param("pageCount") Integer pageCount,
                                 @Param("status") String status);

    @Update("UPDATE document SET status = #{status}, error_message = #{errorMessage} " +
            "WHERE id = #{id}")
    int updateStatusAndError(@Param("id") Long id,
                            @Param("status") String status,
                            @Param("errorMessage") String errorMessage);

    @Select("SELECT * FROM document WHERE id = #{id}")
    Document selectById(Long id);

    @Select("SELECT * FROM document ORDER BY upload_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<Document> selectRecent(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT * FROM document WHERE original_filename LIKE CONCAT('%', #{filename}, '%') " +
            "ORDER BY upload_time DESC")
    List<Document> selectByFilename(String filename);

    @Select("SELECT * FROM document WHERE status = #{status} ORDER BY upload_time DESC")
    List<Document> selectByStatus(String status);

    @Select("SELECT COUNT(*) FROM document WHERE upload_time >= #{startTime}")
    int countSince(LocalDateTime startTime);

    @Delete("DELETE FROM document WHERE id = #{id}")
    int deleteById(Long id);
}
