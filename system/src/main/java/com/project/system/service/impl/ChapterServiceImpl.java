package com.project.system.service.impl;

import com.project.system.domain.Chapter;
import com.project.system.mapper.ChapterMapper;
import com.project.system.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 章节service实现类
 *
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Override
    public Integer getStudentVideo(String taskId, String userId) {
        return baseMapper.getStudentVideo(taskId, userId);
    }
}