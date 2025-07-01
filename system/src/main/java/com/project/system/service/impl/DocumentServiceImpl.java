package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.system.domain.Template;
import com.project.system.domain.UserDocument;
import com.project.system.mapper.DocumentVersionMapper;
import com.project.system.mapper.TemplateMapper;
import com.project.system.mapper.UserDocumentMapper;
import com.project.system.service.DocumentService;
import com.project.system.service.DocumentVersionService;
import com.project.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private UserDocumentMapper userDocumentMapper;

    @Autowired
    private TemplateService templateService;
    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private DocumentVersionMapper documentVersionMapper;

    @Autowired
    private DocumentVersionService documentVersionService;

    //从模板创建文件
    public String createFromTemplate(String templateId,String userId) {
        Template template = templateMapper.selectById(templateId);

        UserDocument doc = new UserDocument();
        doc.setTitle(template.getName());
        doc.setUserId(userId);
        doc.setBaseTemplateId(template.getId());
        doc.setCreatedAt(LocalDateTime.now());
        String shareToken = UUID.randomUUID().toString();
        doc.setShareToken(shareToken);
        doc.setId(shareToken);

        // 从模板创建可编辑副本
        doc.setContent(template.getFileContent());

        // 保存初始版本
        documentVersionService.saveVersion(doc,"0","初始化");
        return shareToken;
    }

    //更新内容
    @Async
    public void updateContent(String docId, byte[] content) {
        UserDocument doc = userDocumentMapper.selectById(docId);
        doc.setContent(content);
        doc.setLastModified(LocalDateTime.now());
        UpdateWrapper<UserDocument> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", docId);
        userDocumentMapper.update(doc,wrapper);
    }

    //返回内容
    public byte[] getContent(String docId) {
        UserDocument doc = userDocumentMapper.selectById(docId);
        return doc.getContent();
    }
}
