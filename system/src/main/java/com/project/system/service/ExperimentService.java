package com.project.system.service;

import com.project.system.domain.Experiment;
import com.project.system.mapper.ExperimentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperimentService {

    @Autowired
    private ExperimentMapper experimentMapper;

    public List<Experiment> getAllEnabledExperiments() {
        return experimentMapper.selectList(null); // 不加条件，查询所有记录
    }
}