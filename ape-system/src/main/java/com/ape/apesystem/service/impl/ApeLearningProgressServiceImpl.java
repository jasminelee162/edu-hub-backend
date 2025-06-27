package com.ape.apesystem.service.impl;

import com.ape.apesystem.domain.ApeLearningProgress;
import com.ape.apesystem.mapper.ApeLearningProgressMapper;
import com.ape.apesystem.service.ApeLearningProgressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApeLearningProgressServiceImpl extends ServiceImpl<ApeLearningProgressMapper, ApeLearningProgress> implements ApeLearningProgressService {

    @Autowired
    private ApeLearningProgressMapper apeLearningProgressMapper;

    @Override
    public void updateOrInsertProgress(ApeLearningProgress progress) {
        // 检查是否已存在该用户的记录
        ApeLearningProgress existingProgress = apeLearningProgressMapper.selectOne(new QueryWrapper<ApeLearningProgress>().lambda().eq(ApeLearningProgress::getUserId, progress.getUserId()));
        if (existingProgress != null) {
            // 如果存在，更新记录
            progress.setId(existingProgress.getId());
            System.out.println("progress.setId(existingProgress.getId())"+existingProgress.getId());
            this.updateById(progress);
        } else {
            // 如果不存在，插入新记录
            System.out.println("progress.setId(progress.getId())"+progress.getId());
            this.save(progress);
        }
    }
}
