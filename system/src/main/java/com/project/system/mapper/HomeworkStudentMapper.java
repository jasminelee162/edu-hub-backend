package com.project.system.mapper;

import com.project.system.domain.HomeworkStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface HomeworkStudentMapper extends BaseMapper<HomeworkStudent> {
    List<HomeworkStudent> selectOnePerChapter(@Param("userId") String userId);
}