package com.project.system.mapper;

import com.project.system.domain.HomeworkStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 学生作业mapper
 * @date 2023/11/22 04:28
 */
public interface HomeworkStudentMapper extends BaseMapper<HomeworkStudent> {
    List<HomeworkStudent> selectOnePerChapter(@Param("userId") String userId);
}