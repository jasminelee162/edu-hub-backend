package com.ape.apesystem.service;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

public interface AIService {
    GenerationResult callWithMessage(String key) throws ApiException, NoApiKeyException, InputRequiredException;
}
