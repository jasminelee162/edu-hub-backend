package com.project.system.mapper;

import com.project.system.domain.Chapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0
 * @description: 章节mapper
 */
public interface ChapterMapper extends BaseMapper<Chapter> {
    Integer getStudentVideo(@Param("taskId") String taskId, @Param("userId") String userId);
}