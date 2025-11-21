package com.yiava.mapper;

import com.yiava.entity.Content;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis Mapper interface for Content entity
 * Provides database operations for content records
 */
@Mapper
public interface ContentMapper {

    /**
     * Insert a new content record
     *
     * @param content the content entity to insert
     * @return the number of rows affected
     */
    @Insert("INSERT INTO content (content) VALUES (#{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Content content);

    /**
     * Find content by ID
     *
     * @param id the content ID
     * @return the content entity if found, null otherwise
     */
    @Select("SELECT id, content, created_at, updated_at FROM content WHERE id = #{id}")
    Content findById(@Param("id") Long id);

    /**
     * Find all content records
     *
     * @return list of all content records
     */
    @Select("SELECT id, content, created_at, updated_at FROM content ORDER BY id DESC")
    java.util.List<Content> findAll();

    /**
     * Update content by ID
     *
     * @param id the content ID
     * @param content the updated content text
     * @return the number of rows affected
     */
    int updateById(@Param("id") Long id, @Param("content") String content);

    /**
     * Delete content by ID
     *
     * @param id the content ID
     * @return the number of rows affected
     */
    int deleteById(@Param("id") Long id);

    /**
     * Count total content records
     *
     * @return the total count
     */
    @Select("SELECT COUNT(*) FROM content")
    int count();
}
