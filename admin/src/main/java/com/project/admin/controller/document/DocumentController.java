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

@Controller
@ResponseBody
@RequestMapping("/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    //创建共享文档(会返回共享链接)
    @PostMapping("/create")
    public Result createDocument(@RequestParam String templateId, @RequestParam String userId) {
        return Result.success(documentService.createFromTemplate(templateId, userId));
    }

    //编辑文档
    @MessageMapping("/{documentId}/edit")
    @SendTo("/topic/document/{documentId}")
    public byte[] editDocument(@PathVariable String documentId,  // 从路径获取文档ID
                               @Payload byte[] documentData) {
        return documentData;
    }


    //请求共享链接
    @MessageMapping("/{Id}/init")
    public void initDocument(@RequestParam String Id,@RequestParam String userId) {
        messagingTemplate.convertAndSendToUser(
                userId,           // 目标用户
                "/queue/init",      // 目标路径（用户专属队列）
                documentService.getContent(Id)  // 消息内容
        );
    }

}

