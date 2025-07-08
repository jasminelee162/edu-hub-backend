package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.common.domain.Result;
import com.project.system.domain.Template;
import com.project.system.domain.UserDocument;
import com.project.system.mapper.DocumentVersionMapper;
import com.project.system.mapper.TemplateMapper;
import com.project.system.mapper.UserDocumentMapper;
import com.project.system.mapper.UserMapper;
import com.project.system.service.DocumentService;
import com.project.system.service.DocumentVersionService;
import com.project.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private UserMapper userMapper;

    //从模板创建文件
    public String createFromTemplate(String templateId,String userId) {
        Template template = templateMapper.selectById(templateId);

        UserDocument doc = new UserDocument();
        doc.setTitle(template.getName());
        doc.setUserId(userId);
        doc.setBaseTemplateId(templateId);
        doc.setCreatedAt(LocalDateTime.now());
        String shareToken = UUID.randomUUID().toString();
        doc.setShareToken(shareToken);
        doc.setId(shareToken);
        doc.setUserCollaboration(userMapper.selectById(userId).getUserName());


        // 从模板创建可编辑副本
        doc.setContent(template.getFileContent());

        // 保存初始版本
        userDocumentMapper.insert(doc);
        documentVersionService.saveVersion(doc,"0","初始化");
        return shareToken;
    }

    //更新内容
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

    public String joinCollaboration(String docId, String userId) {
        UserDocument doc = userDocumentMapper.selectById(docId);
        String name =userMapper.selectById(userId).getUserName();
        String docString=doc.getUserCollaboration();
        List<String> users =List.of(docString.split("#"));
        if (users.contains(name)) {
            return docString;
        }
        String str=doc.getUserCollaboration()+"#"+name;
        doc.setUserCollaboration(str);
        UpdateWrapper<UserDocument> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", docId);
        userDocumentMapper.update(doc,wrapper);
        return str;
    }

    public Result exitCollaboration(String docId, String userId){
        UserDocument doc = userDocumentMapper.selectById(docId);
        String name =userMapper.selectById(userId).getUserName();
        String docString=doc.getUserCollaboration();
        List<String> users =new ArrayList<>(List.of(docString.split("#")));
        if (!users.contains(name)) {
             return Result.success(docString);
        }
       users.remove(name);
        String str="";
        for (int i=0;i<users.size();i++){
            str=str+users.get(i)+"#";
        }
        return Result.success(str);
    }

}
