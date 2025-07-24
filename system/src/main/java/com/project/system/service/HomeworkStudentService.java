package com.project.system.service;

import com.project.system.domain.HomeworkStudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface HomeworkStudentService extends IService<HomeworkStudent> {
    List<HomeworkStudent> selectOnePerChapter(String userId);
}