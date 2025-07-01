package com.project.system.mapper;

import com.project.system.domain.Test;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *  超级管理员
 * @version 1.0
 * @description: 考试mapper
 * 
 */
public interface TestMapper extends BaseMapper<Test> {
    Map<String,Object> getStudentTotalScore(@Param("taskId") String taskId,@Param("userId") String userId);
}