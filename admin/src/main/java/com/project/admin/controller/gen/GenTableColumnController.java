package com.project.admin.controller.gen;

import com.project.system.service.GenTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成字段controller
 * @date 2023/10/10 9:29
 */
@Controller
@ResponseBody
@RequestMapping("genColumn")
public class GenTableColumnController {

    @Autowired
    private GenTableColumnService genTableColumnService;



}
