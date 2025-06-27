package com.ape.apeadmin.controller.ai;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ape.apesystem.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/ai")
public class AIController {
    @Autowired
    private AIService aiService;

    @RequestMapping
    public String chat(@RequestParam String key) throws ApiException, NoApiKeyException, InputRequiredException {
        String a="";
        try {
            GenerationResult result = aiService.callWithMessage(key);
            System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent());
            a=result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 使用日志框架记录异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }

        return a;


    }
}
