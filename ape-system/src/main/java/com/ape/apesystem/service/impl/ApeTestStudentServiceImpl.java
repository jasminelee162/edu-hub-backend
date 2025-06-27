package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeTest;
import com.ape.apesystem.domain.ApeTestStudent;
import com.ape.apesystem.mapper.ApeTestMapper;
import com.ape.apesystem.mapper.ApeTestStudentMapper;
import com.ape.apesystem.service.ApeTestStudentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 用户考试题目service实现类
 * @date 2023/11/24 10:23
 */
@Service
public class ApeTestStudentServiceImpl extends ServiceImpl<ApeTestStudentMapper, ApeTestStudent> implements ApeTestStudentService {
    /*6.27新增 薄弱科目*/
    @Autowired
    private ApeTestMapper apeTestMapper;

    @Override
    public List<Map<String, Object>> getStudentSubjectScores(String userId) {
        // 1. 查询该学生的所有考试记录
        QueryWrapper<ApeTestStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTestStudent::getUserId, userId);
        List<ApeTestStudent> studentScores = this.list(queryWrapper);

        // 2. 查询所有考试信息
        List<ApeTest> allTests = apeTestMapper.selectList(null);

        // 3. 构建课程ID到课程名称的映射
        Map<String, String> testIdToTaskName = allTests.stream()
                .collect(Collectors.toMap(
                        ApeTest::getId,
                        ApeTest::getTaskName
                ));

        // 4. 按课程名称分组，计算学生每个课程的平均分
        Map<String, Double> studentSubjectAvgScores = studentScores.stream()
                .filter(score -> testIdToTaskName.containsKey(score.getTestId()))
                .collect(Collectors.groupingBy(
                        score -> testIdToTaskName.get(score.getTestId()),
                        Collectors.averagingDouble(ApeTestStudent::getPoint)
                ));

        // 5. 计算所有学生每个课程的平均分
        Map<String, Double> allStudentsSubjectAvgScores = allTests.stream()
                .collect(Collectors.groupingBy(
                        ApeTest::getTaskName,
                        Collectors.averagingDouble(test -> {
                            QueryWrapper<ApeTestStudent> examQuery = new QueryWrapper<>();
                            examQuery.lambda().eq(ApeTestStudent::getTestId, test.getId());
                            List<ApeTestStudent> examStudents = this.list(examQuery);
                            return examStudents.stream()
                                    .mapToDouble(ApeTestStudent::getPoint)
                                    .average()
                                    .orElse(0.0);
                        })
                ));

        // 6. 构建结果
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : studentSubjectAvgScores.entrySet()) {
            String subjectName = entry.getKey();
            double studentAvgScore = entry.getValue();
            double allStudentsAvgScore = allStudentsSubjectAvgScores.getOrDefault(subjectName, 0.0);
            String status = studentAvgScore > allStudentsAvgScore ? "优势科目" : "薄弱科目";

            result.add(Map.of(
                    "subject", subjectName,
                    "studentScore", studentAvgScore,
                    "avgScore", allStudentsAvgScore,
                    "status", status
            ));
        }

        return result;
    }
}