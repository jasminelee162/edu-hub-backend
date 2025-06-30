package com.project.admin.controller.experiment;

import com.project.common.domain.Result;
import com.project.system.domain.Experiment;
import com.project.system.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/experiments")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @GetMapping("getAllExperiment")
    public Result getAll() {
        List<Experiment> list = experimentService.getAllEnabledExperiments();
        return Result.success(list);
    }
}