package com.ape.apesystem.service;

import com.ape.apesystem.domain.ApeLearningProgress;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ApeLearningProgressService extends IService<ApeLearningProgress> {
    /**
     * 更新或插入学习进度
     * @param progress 学习进度实体
     */
    void updateOrInsertProgress(ApeLearningProgress progress);
}
