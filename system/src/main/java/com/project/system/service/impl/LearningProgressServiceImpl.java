package com.project.system.service.impl;

import com.project.system.domain.LearningProgress;
import com.project.system.mapper.LearningProgressMapper;
import com.project.system.service.LearningProgressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningProgressServiceImpl extends ServiceImpl<LearningProgressMapper, LearningProgress> implements LearningProgressService {

    @Autowired
    private LearningProgressMapper learningProgressMapper;

    @Override
    public void updateOrInsertProgress(LearningProgress progress) {
        // 检查是否已存在该用户的记录
        LearningProgress existingProgress = learningProgressMapper.selectOne(new QueryWrapper<LearningProgress>().lambda().eq(LearningProgress::getUserId, progress.getUserId()));
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
