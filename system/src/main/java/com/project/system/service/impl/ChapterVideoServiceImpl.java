package com.project.system.service.impl;

import com.project.system.domain.ChapterVideo;
import com.project.system.mapper.ChapterVideoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.system.service.ChapterVideoService;
import org.springframework.stereotype.Service;

/**
 *
 * @version 1.0
 * @description: 章节视频是否观看service实现类
 * 
 */
@Service
public class ChapterVideoServiceImpl extends ServiceImpl<ChapterVideoMapper, ChapterVideo> implements ChapterVideoService {
}