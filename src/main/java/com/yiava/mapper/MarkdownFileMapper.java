package com.yiava.mapper;

import com.yiava.entity.MarkdownFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MyBatis mapper for MarkdownFile entity
 */
public interface MarkdownFileMapper {

    @Insert("INSERT INTO markdown_file (document_id, page_number, file_path, file_size, filename) " +
            "VALUES (#{documentId}, #{pageNumber}, #{filePath}, #{fileSize}, #{filename})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MarkdownFile file);

    @Select("SELECT * FROM markdown_file WHERE id = #{id}")
    MarkdownFile selectById(Long id);

    @Select("SELECT * FROM markdown_file WHERE document_id = #{documentId} ORDER BY page_number ASC")
    List<MarkdownFile> selectByDocumentId(Long documentId);

    @Select("SELECT * FROM markdown_file WHERE document_id = #{documentId} AND page_number = #{pageNumber}")
    MarkdownFile selectByDocumentIdAndPageNumber(@Param("documentId") Long documentId,
                                                  @Param("pageNumber") Integer pageNumber);

    @Select("SELECT COUNT(*) FROM markdown_file WHERE document_id = #{documentId}")
    int countByDocumentId(Long documentId);

    @Delete("DELETE FROM markdown_file WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM markdown_file WHERE document_id = #{documentId}")
    int deleteByDocumentId(Long documentId);
}
