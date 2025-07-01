package com.project.system.service;

import com.project.system.domain.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @version 1.0
 * @description: 章节service
 * 
 */
public interface ChapterService extends IService<Chapter> {
    Integer getStudentVideo(String taskId, String userId);
}