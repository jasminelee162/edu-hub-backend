package com.project.admin.controller.sandbox;

import com.project.common.domain.Result;
import com.project.system.service.SandboxService;
import com.project.system.vo.ExpQuestionVO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping({"/sandbox"})
public class SandboxController {
    @Autowired
    private SandboxService sandboxService;

    public SandboxController() {
    }

    @PostMapping({"/execute"})
    public Result executeQuery(@RequestBody Map<String, Object> jsonMap) {
        String questionId = jsonMap.get("questionId").toString();
        String request = jsonMap.get("request").toString();
        String userStamp = jsonMap.get("userStamp").toString();
        Result result = this.sandboxService.executeQuery(questionId, request, userStamp);
        return result;
    }

    @GetMapping({"/questions"})
    public List<ExpQuestionVO> getAllQuestion() {
        return this.sandboxService.getAllExpQuestion();
    }

    @GetMapping({"/description"})
    public Result getDescription(@RequestParam String questionId) {
        return Result.success(this.sandboxService.getDescription(questionId));
    }

    @GetMapping({"/title"})
    public Result getTitle(@RequestParam String questionId) {
        return Result.success(this.sandboxService.getTitle(questionId));
    }
}
