package com.ape.apesystem.service.impl;

import com.ape.apecommon.domain.Result;
import com.ape.apesystem.domain.ExpQuestion;
import com.ape.apesystem.mapper.ExpQuestionMapper;
import com.ape.apesystem.service.SandboxInitializeService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SandboxInitializeImpl implements SandboxInitializeService {
    @Autowired
    private ExpQuestionMapper expQuestionMapper;
    private final Map<String, Boolean> initializedQuestions = new ConcurrentHashMap();

    public SandboxInitializeImpl() {
    }

    @Transactional
    public Result initializeSandbox(String questionId, String userStamp) {
        if (this.initializedQuestions.containsKey(questionId)) {
            return Result.success();
        } else {
            ExpQuestion question;
            try {
                question = (ExpQuestion)this.expQuestionMapper.selectById(questionId);
            } catch (Exception var9) {
                return Result.fail("未找到该题目序号");
            }

            String dbName = "mem:sandbox_" + userStamp + "_" + questionId;
            DataSource ds = DataSourceBuilder.create().url("jdbc:h2:" + dbName + ";DB_CLOSE_DELAY=-1").build();
            JdbcTemplate jt = new JdbcTemplate(ds);
            List<String> tableSchemasList = Arrays.asList(question.getTableSchemas().split("#"));
            Objects.requireNonNull(jt);
            tableSchemasList.forEach(jt::execute);
            List<String> dataScriptList = Arrays.asList(question.getDataScript().split("#"));
            Objects.requireNonNull(jt);
            dataScriptList.forEach(jt::execute);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                jt.execute("SHUTDOWN");
            }));
            this.initializedQuestions.put(questionId, true);
            return Result.success(jt);
        }
    }
}