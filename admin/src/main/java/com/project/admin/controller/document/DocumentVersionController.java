package com.project.admin.controller.document;

import com.project.common.domain.Result;
import com.project.system.domain.UserDocument;
import com.project.system.mapper.UserDocumentMapper;
import com.project.system.service.DocumentService;
import com.project.system.service.DocumentVersionService;
import com.project.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Controller
@ResponseBody
@RequestMapping("/documentVersion")
public class DocumentVersionController {

    @Autowired
    private DocumentVersionService documentVersionService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserDocumentMapper userDocumentMapper;


    //回滚
    @GetMapping("/rollback")
    public Result rollback(@RequestParam String versionId,@RequestParam String userId) {
        System.out.println("aaaaaaaaaaaa:"+userId);
        System.out.println("bbbbbbbbbbbb:"+versionId);
        if (!userDocumentMapper.selectById(versionId.substring(0,versionId.length()-1)).getUserId().equals(userId)) {
            return Result.fail("只有共享文档创建者才可执行历史回滚功能");
        }
        documentService.updateContent(versionId.substring(0,versionId.length()-1),documentVersionService.getVersionById(versionId));
        return Result.success(documentVersionService.getVersionById(versionId));
    }

    //获得所有版本
    @GetMapping("/all")
    public Result getAllVersions(@RequestParam String documentId) {
        System.out.println("uuuuuuuuuuuuuuuuuuu："+documentVersionService.getAllVersion(documentId));
        return Result.success(documentVersionService.getAllVersion(documentId));
    }

    //版本记录
    @PostMapping("/record")
    public Result record(@RequestParam String documentId,@RequestParam byte[] content, @RequestParam String changeNote) {
        UserDocument userDocument = new UserDocument();
        userDocument.setId(documentId);
        userDocument.setContent(content);
        String id=String.valueOf(documentVersionService.getAllVersion(documentId).size());
        documentVersionService.saveVersion(userDocument,id,changeNote);
        return Result.success("操作成功");
    }

}