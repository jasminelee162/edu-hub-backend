package com.project.system.mapper;

import com.project.system.domain.Homework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @version 1.0
 * @description: 作业mapper
 * 
 */
public interface HomeworkMapper extends BaseMapper<Homework> {
    List<String> getStudentHomeWork(@Param("taskId") String taskId, @Param("userId") String userId);

    List<String> getTotalAssignCount(@Param("taskId") String taskId);
}