package com.project.admin.controller.document;


import com.project.common.domain.Result;
import com.project.system.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
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
    public byte[] editDocument(@DestinationVariable String documentId,  // 从路径获取文档ID
                               @Payload byte[] documentData) {
        return documentData;
    }



    //共享初始化
    @MessageMapping("/{documentId}/init")
    public void initDocument(@DestinationVariable String documentId,@Header("simpUser") Principal principal
                             ) {
        if (principal == null) {
            throw new IllegalStateException("未认证用户");
        }

        String userId = principal.getName();
        System.out.println("处理文档初始化, documentId: " + documentId +
                ", userId: " + userId);
        List<String> users=documentService.joinCollaboration(documentId,userId);
        messagingTemplate.convertAndSendToUser(
                userId,           // 目标用户
                "/queue/init",      // 目标路径（用户专属队列）
                documentService.getContent(documentId)  // 消息内容
        );
        messagingTemplate.convertAndSend(
                "/topic/document/" + documentId + "/join",
                users);
    }
}