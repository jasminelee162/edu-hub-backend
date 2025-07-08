package com.project.system.mapper;

import com.project.system.domain.TaskStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 课程报名mapper
 * @date 2023/11/21 03:15
 */
public interface TaskStudentMapper extends BaseMapper<TaskStudent> {
    @Select("SELECT COUNT(DISTINCT user_id) FROM task_student WHERE task_id = #{taskId} AND state = 0")
    int countDistinctUserByTaskId(@Param("taskId") String taskId);
}