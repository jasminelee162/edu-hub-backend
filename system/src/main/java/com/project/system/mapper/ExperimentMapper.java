package com.project.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.system.domain.Experiment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExperimentMapper extends BaseMapper<Experiment> {
}