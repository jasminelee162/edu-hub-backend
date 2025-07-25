package com.project.admin.controller.document;

import com.project.common.domain.Result;
import com.project.system.mapper.UserDocumentMapper;
import com.project.system.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody
@Slf4j
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /*@Autowired
    private SimpMessageSendingOperations messagingTemplate;*/
    @Autowired
    private UserDocumentMapper userDocumentMapper;

    // 创建共享文档(会返回共享ID)
    @PostMapping("/create")
    public Result createDocument(@RequestBody Map<String, Object> jsonMap) {
        String templateId = jsonMap.get("templateId").toString();
        String userId = jsonMap.get("userId").toString();
        return Result.success(documentService.createFromTemplate(templateId, userId));
    }

    @GetMapping("/members")
    public Result members(@RequestParam String documentId) {
        return Result.success(documentService.getUserList(documentId));
    }

    @GetMapping("/documentInit")
    public Result documentInit(@RequestParam String documentId) {
        return Result.success(documentService.getDocumentVO(documentId));
    }

    // 编辑文档
    @MessageMapping("/{documentId}/edit")
    @SendTo("/topic/document/{documentId}")
    public byte[] editDocument(@DestinationVariable String documentId,
                               @Payload byte[] documentData) {
        return documentData;
    }

    @GetMapping("/confirm")
    public Result confirmDocument(@RequestParam("documentId") String documentId) {
        if(userDocumentMapper.selectById(documentId)==null){
            return Result.fail("共享文档号不存在");
        }
        return Result.success();
    }




}