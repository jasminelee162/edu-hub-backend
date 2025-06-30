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
        System.out.println("开始查询学生的所有考试记录...");
        // 1. 查询该学生的所有考试记录
        QueryWrapper<ApeTestStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ApeTestStudent::getUserId, userId);
        List<ApeTestStudent> studentScores = this.list(queryWrapper);
        System.out.println("查询到 " + studentScores.size() + " 条考试记录。");

        // 2. 查询所有考试信息
        List<ApeTest> allTests = apeTestMapper.selectList(null);
        System.out.println("查询到 " + allTests.size() + " 条考试信息。");

        // 3. 构建考试ID到课程名称的映射
        Map<String, String> testIdToTaskName = allTests.stream()
                .collect(Collectors.toMap(
                        ApeTest::getId,
                        ApeTest::getTaskName,
                        (existing, replacement) -> existing
                ));

        // 4. 按考试(testId)分组，计算每次考试的总分
        Map<String, Double> examTotalScores = studentScores.stream()
                .collect(Collectors.groupingBy(
                        ApeTestStudent::getTestId,
                        Collectors.summingDouble(ApeTestStudent::getPoint)
                ));

        // 5. 按课程(taskName)分组，计算每个课程的总分和考试次数
        Map<String, Double> courseTotalScores = new HashMap<>();
        Map<String, Integer> courseExamCounts = new HashMap<>();
        // 存储每个课程的所有考试成绩
        Map<String, List<Double>> courseAllScores = new HashMap<>();

        examTotalScores.forEach((testId, totalScore) -> {
            if (testIdToTaskName.containsKey(testId)) {
                String taskName = testIdToTaskName.get(testId);
                courseTotalScores.merge(taskName, totalScore, Double::sum);
                courseExamCounts.merge(taskName, 1, Integer::sum);
                // 存储每个考试的具体分数
                courseAllScores.computeIfAbsent(taskName, k -> new ArrayList<>()).add(totalScore);
            }
        });

        // 6. 计算每个课程的平均分（课程总分/考试次数）
        Map<String, Double> studentSubjectAvgScores = new HashMap<>();
        courseTotalScores.forEach((taskName, totalScore) -> {
            int examCount = courseExamCounts.getOrDefault(taskName, 1);
            double avgScore = totalScore / examCount;
            studentSubjectAvgScores.put(taskName, avgScore);
        });
        System.out.println("计算每个课程的平均分完成。");

        // 如果学生参加的课程少于2个，则不返回结果
        if (studentSubjectAvgScores.size() < 2) {
            System.out.println("学生参加的课程少于2个，不返回结果。");
            return Collections.emptyList();
        }

        // 7. 找出学生得分最高和最低的课程
        Optional<Map.Entry<String, Double>> maxScoreEntry = studentSubjectAvgScores.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        Optional<Map.Entry<String, Double>> minScoreEntry = studentSubjectAvgScores.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue());

        // 8. 构建结果
        List<Map<String, Object>> result = new ArrayList<>();

        // 添加优势课程(得分最高的课程)
        maxScoreEntry.ifPresent(entry -> {
            result.add(Map.of(
                    "subject", entry.getKey(),
                    "studentScore", entry.getValue(),
                    "avgScore", entry.getValue(),
                    "status", "优势科目"
            ));
            System.out.println("添加优势课程：" + entry.getKey() + "，得分：" + entry.getValue());
        });

        // 添加薄弱课程(得分最低的课程)
        minScoreEntry.ifPresent(entry -> {
            // 只有当最高分和最低分不是同一课程时才添加薄弱课程
            if (maxScoreEntry.isPresent() && !maxScoreEntry.get().getKey().equals(entry.getKey())) {
                List<Double> scores = courseAllScores.getOrDefault(entry.getKey(), Collections.emptyList());
                String scoresString = scores.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining("，"));

                result.add(Map.of(
                        "subject", entry.getKey(),
                        "studentScore", scoresString,
                        "avgScore", entry.getValue(),
                        "status", "薄弱科目"
                ));
                System.out.println("添加薄弱课程：" + entry.getKey() + "，得分：" + scoresString);
            }
        });

        System.out.println("结果构建完成，返回结果。");
        return result;
    }



    /* 6.28 新增 错题集*/
    @Override
    public List<ApeTestStudent> getWrongAnswers(String userId) {
        return baseMapper.selectWrongAnswers(userId);
    }
}