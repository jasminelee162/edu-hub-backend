package com.project.admin.controller.gen;

import com.project.system.service.GenTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version 1.0
 * @description: 代码生成字段controller
 */
@Controller
@ResponseBody
@RequestMapping("genColumn")
public class GenTableColumnController {

    @Autowired
    private GenTableColumnService genTableColumnService;



}
