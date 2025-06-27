package com.ape.apesystem.mapper;

import com.ape.apesystem.domain.ApeTestStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目mapper
 * @date 2023/11/24 10:23
 */
public interface ApeTestStudentMapper extends BaseMapper<ApeTestStudent> {
    @Select("SELECT user_id, MAX(update_time) as update_time " +
            "FROM ape_test_student " +
            "WHERE test_id = #{testId} " +
            "GROUP BY user_id " +
            "ORDER BY update_time ASC")
    List<ApeTestStudent> getTestStudent(@Param("testId") String testId);

    /*6.28 新增 错题集*/
    @Select("SELECT ts.* FROM ape_test_student ts " +
            "JOIN ape_test_item ti ON ts.item_id = ti.id " +
            "WHERE ts.user_id = #{userId} AND ts.point < ti.score")
    List<ApeTestStudent> selectWrongAnswers(@Param("userId") String userId);
}