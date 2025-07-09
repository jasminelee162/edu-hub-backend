package com.project.admin.controller.ai;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.project.common.domain.Result;
import com.project.system.domain.TestStudent;
import com.project.system.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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



    //AI主观题批改
    @RequestMapping("/grades")
    public Result gradingPapers(@RequestBody List<TestStudent> testStudent) throws ApiException, NoApiKeyException, InputRequiredException {
        List<Integer> grades=new ArrayList<>();
        int count=0;
        for(TestStudent t:testStudent){
            count++;
            GenerationResult result=aiService.gradingPapers(t);
            grades.add(Integer.valueOf((result.getOutput().getChoices().get(0).getMessage().getContent())));
        }
        System.out.println("次数："+count);
        System.out.println("grade大小："+grades.size());
        return Result.success(grades);
    }
}
