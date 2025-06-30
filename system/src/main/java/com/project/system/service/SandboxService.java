package com.project.system.service;

import com.project.common.domain.Result;
import com.project.system.vo.ExpQuestionVO;
import java.util.List;

public interface SandboxService {
    String getTitle(String questionId);

    String getDescription(String questionId);

    List<ExpQuestionVO> getAllExpQuestion();

    Result executeQuery(String questionId, String userSql, String userStamp);
}
