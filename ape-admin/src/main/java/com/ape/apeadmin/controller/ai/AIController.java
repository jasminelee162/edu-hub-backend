package com.ape.apeadmin.controller.ai;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ape.apecommon.domain.Result;
import com.ape.apesystem.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@CrossOrigin(origins = "http://127.0.0.1:3001")
@Controller
@ResponseBody
@RequestMapping("/ai")
public class AIController {
    @Autowired
    private AIService aiService;

    @RequestMapping("/chat")
    public Result chat(@RequestParam String key) throws ApiException, NoApiKeyException, InputRequiredException {
        String resp="";
        try {
            GenerationResult result = aiService.callWithMessage(key);
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
            resp=result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        return Result.success(resp);
    }

    @RequestMapping("/suggestion")
    public Result suggestion(@RequestParam String id) throws ApiException, NoApiKeyException, InputRequiredException {
        String resp="";
        try {
            GenerationResult result = aiService.studySuggestion(id);
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
            resp=result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        return Result.success(resp);
    }

}
