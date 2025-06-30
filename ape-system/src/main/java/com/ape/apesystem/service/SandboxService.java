package com.ape.apesystem.service;

import com.ape.apecommon.domain.Result;
import com.ape.apesystem.vo.ExpQuestionVO;
import java.util.List;

public interface SandboxService {
    String getTitle(String questionId);

    String getDescription(String questionId);

    List<ExpQuestionVO> getAllExpQuestion();

    Result executeQuery(String questionId, String userSql, String userStamp);
}
