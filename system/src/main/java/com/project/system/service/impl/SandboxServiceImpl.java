package com.project.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.project.common.domain.Result;
import com.project.system.domain.ExpQuestion;
import com.project.system.mapper.ExpQuestionMapper;
import com.project.system.service.SandboxInitializeService;
import com.project.system.service.SandboxService;
import com.project.system.service.SqlSecurityService;
import com.project.system.vo.ExpQuestionVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SandboxServiceImpl implements SandboxService {
    @Autowired
    private SqlSecurityService sqlSecurityService;
    @Autowired
    private SandboxInitializeService sandboxInitializeService;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExpQuestionMapper expQuestionMapper;

    @Transactional(
            timeout = 5
    )
    public Result executeQuery(String questionId, String userSql, String userStamp) {
        System.out.println("aaaaaaaaaaa" + questionId);
        System.out.println("bbbbbbbbbbb" + userSql);
        System.out.println("ccccccccccc" + userStamp);
        sqlSecurityService.validate(userSql);
        Result result = sandboxInitializeService.initializeSandbox(questionId, userStamp);
        if (result.getData() instanceof JdbcTemplate) {
            this.jdbcTemplate = (JdbcTemplate)result.getData();
        }

        try {
            List<Map<String, Object>> selectResult = this.jdbcTemplate.queryForList(userSql);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaahello查询到的答案：" + selectResult);
            List<Map<String, Object>> expected = this.getExpectedResult(((ExpQuestion)this.expQuestionMapper.selectById(questionId)).getStandardAnswer());
            System.out.println("保准答案：" + expected);
            boolean isCorrect = this.compareResults(selectResult, expected);
            return isCorrect ? Result.success("查询成功，恭喜通关！") : Result.fail("查询执行错误");
        } catch (Exception var8) {
            return Result.fail("查询执行错误");
        }
    }

    public String getTitle(String questionId) {
        return ((ExpQuestion)this.expQuestionMapper.selectById(questionId)).getTitle();
    }

    public String getDescription(String questionId) {
        return ((ExpQuestion)this.expQuestionMapper.selectById(questionId)).getDescription();
    }

    public List<ExpQuestionVO> getAllExpQuestion() {
        return this.expQuestionToVO(this.expQuestionMapper.selectList((Wrapper)null));
    }

    private List<ExpQuestionVO> expQuestionToVO(List<ExpQuestion> expQuestions) {
        List<ExpQuestionVO> expQuestionVOList = new ArrayList();
        Iterator var3 = expQuestions.iterator();

        while(var3.hasNext()) {
            ExpQuestion expQuestion = (ExpQuestion)var3.next();
            ExpQuestionVO expQuestionVO = new ExpQuestionVO();
            expQuestionVO.setId(expQuestion.getId());
            expQuestionVO.setTitle(expQuestion.getTitle());
            expQuestionVO.setDifficulty(expQuestion.getDifficulty());
            expQuestionVOList.add(expQuestionVO);
        }

        return expQuestionVOList;
    }

    private boolean compareResults(List<Map<String, Object>> actual, List<Map<String, Object>> expected) {
        if (actual.size() != expected.size()) {
            return false;
        } else {
            List<Map<String, Object>> expectedCopy = new ArrayList(expected);
            Iterator var4 = actual.iterator();

            boolean foundMatch;
            do {
                if (!var4.hasNext()) {
                    return true;
                }

                Map<String, Object> actualMap = (Map)var4.next();
                foundMatch = false;
                Iterator<Map<String, Object>> expectedIterator = expectedCopy.iterator();

                while(expectedIterator.hasNext()) {
                    Map<String, Object> expectedMap = (Map)expectedIterator.next();
                    if (this.compareMaps(actualMap, expectedMap)) {
                        expectedIterator.remove();
                        foundMatch = true;
                        break;
                    }
                }
            } while(foundMatch);

            return false;
        }
    }

    private boolean compareMaps(Map<String, Object> map1, Map<String, Object> map2) {
        if (!map1.keySet().equals(map2.keySet())) {
            return false;
        } else {
            PrintStream var10000 = System.out;
            Set var10001 = map1.keySet();
            var10000.println("有几个：" + var10001 + "\n" + map2.keySet() + "\n");
            Iterator var3 = map1.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                Object value1 = map1.get(key);
                Object value2 = map2.get(key);
                if (!(value1 instanceof Timestamp) && !(value1 instanceof Date)) {
                    if (value2 instanceof Timestamp || value2 instanceof Date) {
                        System.out.println("啊啊啊啊啊啊啊啊啊2");
                        value2 = value2.toString().trim();
                        if (!value2.equals(value1.toString().trim())) {
                            return false;
                        }
                    }
                } else {
                    System.out.println("啊啊啊啊啊啊啊啊啊1");
                    value1 = value1.toString().trim();
                    if (!value1.equals(value2.toString().trim())) {
                        return false;
                    }
                }

                if (value1 instanceof Number && value2 instanceof Number) {
                    double d1 = ((Number)value1).doubleValue();
                    double d2 = ((Number)value2).doubleValue();
                    if (!String.format("%.1f", d1).equals(String.format("%.1f", d2))) {
                        return false;
                    }
                } else if (!Objects.equals(value1, value2)) {
                    return false;
                }

                var10000 = System.out;
                String var11 = value1.getClass().getName();
                var10000.println("value1的类型：" + var11 + value1 + "\n");
                var10000 = System.out;
                var11 = value2.getClass().getName();
                var10000.println("value2的类型：" + var11 + value1 + "\n");
            }

            return true;
        }
    }

    private List<Map<String, Object>> getExpectedResult(String jsonString) {
        List<Map<String, Object>> productList = (List)JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
        }, new JSONReader.Feature[0]);
        return productList;
    }
}