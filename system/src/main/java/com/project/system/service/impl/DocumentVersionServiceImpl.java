package com.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.system.domain.DocumentVersion;
import com.project.system.domain.UserDocument;
import com.project.system.mapper.DocumentVersionMapper;
import com.project.system.service.DocumentVersionService;
import com.project.system.vo.DocumentVersionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentVersionServiceImpl implements DocumentVersionService {

    @Autowired
    private DocumentVersionMapper documentVersionMapper;

    //记录版本
    public void saveVersion(UserDocument doc, String id, String changeNote) {
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setId(doc.getId()+id);
        documentVersion.setContent(doc.getContent());
        documentVersion.setDocumentId(doc.getId());
        documentVersion.setChangeNote(changeNote);
        documentVersion.setChangeVersion(id);
        documentVersion.setCreatedAt(LocalDateTime.now());
        documentVersionMapper.insert(documentVersion);
    }

    //返回所有版本
    public List<DocumentVersionVO> getAllVersion(String documentId) {
        LambdaQueryWrapper<DocumentVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DocumentVersion::getDocumentId, documentId);
        List <DocumentVersion> versionList=documentVersionMapper.selectList(queryWrapper);
        List<DocumentVersionVO> voList=documentVersionToVO(versionList);
        return voList;
    }

    private List<DocumentVersionVO> documentVersionToVO(List<DocumentVersion> documentVersions){
        List<DocumentVersionVO> voList=new ArrayList<>();
        for(DocumentVersion documentVersion:documentVersions){
            DocumentVersionVO documentVersionVO=new DocumentVersionVO();
            documentVersionVO.setDocumentId(documentVersion.getId());
            documentVersionVO.setChangeVersion(documentVersion.getChangeVersion());
            documentVersionVO.setChangeNote(documentVersion.getChangeNote());
            voList.add(documentVersionVO);
        }
        return voList;
    }

    //返回某个版本的内容
    public byte[] getVersionById(String id) {

        return documentVersionMapper.selectById(id).getContent();
    }
}