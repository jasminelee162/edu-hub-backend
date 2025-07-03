package com.project.admin.controller.document;


import com.project.common.domain.Result;
import com.project.system.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    //创建共享文档(会返回共享ID)
    @PostMapping("/create")
    public Result createDocument(@RequestBody Map<String, Object> jsonMap) {
        String templateId = jsonMap.get("templateId").toString();
        String userId = jsonMap.get("userId").toString();
        return Result.success(documentService.createFromTemplate(templateId, userId));
    }

    //编辑文档
    @MessageMapping("/{documentId}/edit")
    @SendTo("/topic/document/{documentId}")
    public byte[] editDocument(@PathVariable String documentId,  // 从路径获取文档ID
                               @Payload byte[] documentData) {
        return documentData;
    }



    //共享初始化
    @MessageMapping("/{documentId}/init")
    public void initDocument(@PathVariable String documentId,@RequestParam String userId) {
        messagingTemplate.convertAndSendToUser(
                userId,           // 目标用户
                "/queue/init",      // 目标路径（用户专属队列）
                documentService.getContent(documentId)  // 消息内容
        );
        documentService.joinCollaboration(documentId,userId);
    }

    @PostMapping("/exit/{documentId}")
    @SendTo("/topic/document/{documentId}")
    public Result exitDocument(@PathVariable String documentId,@RequestParam String userId) {
        return Result.success(documentService.exitCollaboration(documentId,userId));
    }

}

