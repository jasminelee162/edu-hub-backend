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

import java.util.*;
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

        // 3. 构建考试ID到课程名称的映射
        Map<String, String> testIdToTaskName = allTests.stream()
                .collect(Collectors.toMap(
                        ApeTest::getId,
                        ApeTest::getTaskName,
                        (existing, replacement) -> existing
                ));

        // 4. 按课程名称分组，计算学生每个课程的平均分
        Map<String, Double> studentSubjectAvgScores = studentScores.stream()
                .filter(score -> testIdToTaskName.containsKey(score.getTestId()))
                .collect(Collectors.groupingBy(
                        score -> testIdToTaskName.get(score.getTestId()),
                        Collectors.averagingDouble(ApeTestStudent::getPoint)
                ));

        // 如果学生参加的科目少于2个，则不返回结果
        if (studentSubjectAvgScores.size() < 2) {
            return Collections.emptyList();
        }

        // 5. 找出学生得分最高和最低的科目
        Optional<Map.Entry<String, Double>> maxScoreEntry = studentSubjectAvgScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        Optional<Map.Entry<String, Double>> minScoreEntry = studentSubjectAvgScores.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());

        // 6. 构建结果
        List<Map<String, Object>> result = new ArrayList<>();

        // 添加优势科目(得分最高的科目)
        maxScoreEntry.ifPresent(entry -> {
            result.add(Map.of(
                    "subject", entry.getKey(),
                    "studentScore", entry.getValue(),
                    "status", "优势科目"
            ));
        });

        // 添加薄弱科目(得分最低的科目)
        minScoreEntry.ifPresent(entry -> {
            // 只有当最高分和最低分不是同一科目时才添加薄弱科目
            if (!maxScoreEntry.get().getKey().equals(entry.getKey())) {
                result.add(Map.of(
                        "subject", entry.getKey(),
                        "studentScore", entry.getValue(),
                        "status", "薄弱科目"
                ));
            }
        });

        return result;
    }
}