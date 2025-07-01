package com.project.system.service;

import com.project.system.domain.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节service
 * @date 2023/11/17 07:14
 */
public interface ChapterService extends IService<Chapter> {
    Integer getStudentVideo(String taskId, String userId);
}