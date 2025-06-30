package com.project.system.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

public interface AIService {
    GenerationResult callWithMessage(String key) throws ApiException, NoApiKeyException, InputRequiredException;
    GenerationResult studySuggestion(String key) throws ApiException,NoApiKeyException, InputRequiredException;
}
