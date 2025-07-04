package com.project.admin.controller.document;

import com.project.common.domain.Result;
import com.project.system.domain.UserDocument;
import com.project.system.service.DocumentService;
import com.project.system.service.DocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/documentVersion")
public class DocumentVersionController {

    @Autowired
    private DocumentVersionService documentVersionService;
    @Autowired
    private DocumentService documentService;


    //回滚
    @GetMapping("/rollback")
    public Result rollback(String id) {
        documentService.updateContent(id,documentVersionService.getVersionById(id));
        return Result.success(documentVersionService.getVersionById(id));
    }

    //获得所有版本
    @GetMapping("/all")
    public Result getAllVersions(@RequestParam String documentId) {
        return Result.success(documentVersionService.getAllVersion(documentId));
    }

    //版本记录
    @PostMapping("/record")
    public void record(@RequestParam String documentId,@RequestParam byte[] content, @RequestParam String changeNote) {
        UserDocument userDocument = new UserDocument();
        userDocument.setId(documentId);
        userDocument.setContent(content);
        String id=String.valueOf(documentVersionService.getAllVersion(documentId).size());
        documentVersionService.saveVersion(userDocument,id,changeNote);
    }

}


