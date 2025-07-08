package com.project.system.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.project.system.domain.TestStudent;

public interface AIService {
    GenerationResult callWithMessage(String key) throws ApiException, NoApiKeyException, InputRequiredException;
    GenerationResult studySuggestion(String key) throws ApiException,NoApiKeyException, InputRequiredException;
    GenerationResult gradingPapers(TestStudent testStudent)throws ApiException,NoApiKeyException, InputRequiredException;
}
