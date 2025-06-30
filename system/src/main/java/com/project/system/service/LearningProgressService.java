package com.project.system.service;

import com.project.system.domain.LearningProgress;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LearningProgressService extends IService<LearningProgress> {
    /**
     * 更新或插入学习进度
     * @param progress 学习进度实体
     */
    void updateOrInsertProgress(LearningProgress progress);
}
