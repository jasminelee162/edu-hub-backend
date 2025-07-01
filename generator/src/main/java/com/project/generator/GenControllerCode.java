package com.project.generator;

import com.project.common.utils.HumpUtils;
import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  
 * @version 1.0
 * @description: controller代码生成类
 * 16:46
 */
public class GenControllerCode {

    public static String genController(GenTable genTable, List<GenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        String name = genTable.getTableName().substring(genTable.getTableName().lastIndexOf("_") + 1);
        String camel = HumpUtils.toSmallCamel(genTable.getTableName(), "_");
        String toCamel = HumpUtils.toCamel(genTable.getTableName(), "_");
        builder.append("package com.ape.apeadmin.controller.").append(name).append(";\n").append("\n").append("import annotation.com.project.common.Log;\n").append("import domain.com.project.common.Result;\n").append("import enums.com.project.common.BusinessType;\n").append("import enums.com.project.common.ResultCode;\n").append("import com.ape.apesystem.domain.").append(genTable.getClassName()).append(";\n").append("import com.ape.apesystem.service.").append(genTable.getClassName()).append("Service;\n").append("import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\n").append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n").append("import org.apache.commons.lang3.StringUtils;\n").append("import org.springframework.beans.factory.annotation.Autowired;\n").append("import org.springframework.stereotype.Controller;\n").append("import org.springframework.transaction.annotation.Transactional;\n").append("import org.springframework.web.bind.annotation.*;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " *  ").append(genTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(genTable.getBusinessName()).append("controller\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("@Controller\n" + "@ResponseBody\n" + "@RequestMapping(\"").append(name).append("\")\n").append("public class ").append(genTable.getClassName()).append("Controller {\n\n");
        builder.append("     @Autowired\n" + "     private ").append(genTable.getClassName()).append("Service ").append(camel).append("Service;\n\n");
        //拼接分页方法
        builder.append("     /** 分页获取").append(genTable.getBusinessName()).append(" */\n").append("     @Log(name = \"分页获取").append(genTable.getBusinessName()).append("\", type = BusinessType.OTHER)\n").append("     @PostMapping(\"get").append(toCamel).append("Page\")\n").append("     public Result get"+ toCamel +"Page(@RequestBody ").append(genTable.getClassName()).append(" ").append(camel).append(") {\n");
        builder.append("         Page<").append(genTable.getClassName()).append("> page = new Page<>(").append(camel).append(".getPageNumber(),").append(camel).append(".getPageSize());\n").append("         QueryWrapper<").append(genTable.getClassName()).append("> queryWrapper = new QueryWrapper<>();\n");
        List<GenTableColumn> list = new ArrayList<>();
        //先遍历出来查询字段
        for (int i = 0; i < columnList.size(); i++) {
            GenTableColumn genTableColumn = columnList.get(i);
            if (genTableColumn.getIsQuery() == 1) {
                list.add(genTableColumn);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            GenTableColumn genTableColumn = list.get(i);
            if (i == 0) {
                builder.append("         queryWrapper.lambda()\n");
            }
            String com = "";
            String s = HumpUtils.toCamel(genTableColumn.getJavaField(), "_");
            if ("String".equals(genTableColumn.getJavaType())) {
                com = "StringUtils.isNotBlank("+ camel +".get"+ s +"())";
            } else {
                com = camel +".get"+ s +"() != null";
            }
            if ("EQ".equals(genTableColumn.getQueryType())) {
                builder.append("                .eq(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("NE".equals(genTableColumn.getQueryType())) {
                builder.append("                .ne(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("GT".equals(genTableColumn.getQueryType())) {
                builder.append("                .gt(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("GTE".equals(genTableColumn.getQueryType())) {
                builder.append("                .ge(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("LT".equals(genTableColumn.getQueryType())) {
                builder.append("                .lt(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("LTE".equals(genTableColumn.getQueryType())) {
                builder.append("                .le(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("LIKE".equals(genTableColumn.getQueryType())) {
                builder.append("                .like(").append(com).append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("())");
            }
            if ("BETWEEN".equals(genTableColumn.getQueryType()) && "Date".equals(genTableColumn.getJavaType())) {
                builder.append("                .ge(").append(camel).append(".get").append(s).append("StartTime() != null").append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("StartTime").append("())\n");
                builder.append("                .le(").append(camel).append(".get").append(s).append("EndTime() != null").append(",").append(genTable.getClassName()).append("::get").append(s).append(",").append(camel).append(".get").append(s).append("EndTime").append("())");
            }
            if (i == list.size() -1 ) {
                builder.append(";\n");
            } else {
                builder.append("\n");
            }
        }
        //开始查询
        builder.append("         Page<").append(genTable.getClassName()).append("> ").append(camel).append("Page = ").append(camel).append("Service.page(page, queryWrapper);\n");
        builder.append("         return Result.success(").append(camel).append("Page);\n    }\n\n");
        //根据id查询
        builder.append("     /** 根据id获取").append(genTable.getBusinessName()).append(" */\n").append("     @Log(name = \"根据id获取").append(genTable.getBusinessName()).append("\", type = BusinessType.OTHER)\n").append("     @GetMapping(\"get").append(genTable.getClassName()).append("ById\")\n").append("     public Result get").append(genTable.getClassName()).append("ById(@RequestParam(\"id\")String id) {\n").append("         ").append(genTable.getClassName()).append(" ").append(camel).append(" = ").append(camel).append("Service.getById(id);\n").append("         return Result.success(").append(camel).append(");\n").append("     }\n\n");
        //保存
        builder.append("     /** 保存").append(genTable.getBusinessName()).append(" */\n").append("     @Log(name = \"保存").append(genTable.getBusinessName()).append("\", type = BusinessType.INSERT)\n").append("     @PostMapping(\"save").append(genTable.getClassName()).append("\")\n").append("     public Result save").append(genTable.getClassName()).append("(@RequestBody ").append(genTable.getClassName()).append(" ").append(camel).append(") {\n").append("         boolean save = ").append(camel).append("Service.save(").append(camel).append(");\n").append("         if (save) {\n").append("             return Result.success();\n").append("         } else {\n").append("             return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());\n").append("         }\n").append("     }\n\n");
        //编辑
        builder.append("     /** 编辑").append(genTable.getBusinessName()).append(" */\n").append("     @Log(name = \"编辑").append(genTable.getBusinessName()).append("\", type = BusinessType.UPDATE)\n").append("     @PostMapping(\"edit").append(genTable.getClassName()).append("\")\n").append("     public Result edit").append(genTable.getClassName()).append("(@RequestBody ").append(genTable.getClassName()).append(" ").append(camel).append(") {\n").append("         boolean save = ").append(camel).append("Service.updateById(").append(camel).append(");\n").append("         if (save) {\n").append("             return Result.success();\n").append("         } else {\n").append("             return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());\n").append("         }\n").append("     }\n\n");
        //删除
        builder.append("     /** 删除").append(genTable.getBusinessName()).append(" */\n").append("     @GetMapping(\"remove").append(genTable.getClassName()).append("\")\n").append("     @Log(name = \"删除").append(genTable.getBusinessName()).append("\", type = BusinessType.DELETE)\n").append("     public Result remove").append(genTable.getClassName()).append("(@RequestParam(\"ids\")String ids) {\n").append("         if (StringUtils.isNotBlank(ids)) {\n").append("             String[] asList = ids.split(\",\");\n").append("             for (String id : asList) {\n").append("                 ").append(camel).append("Service.removeById(id);\n").append("             }\n").append("             return Result.success();\n").append("         } else {\n").append("             return Result.fail(\"").append(genTable.getBusinessName()).append("id不能为空！\");\n").append("         }\n").append("     }\n\n");
        builder.append("}");
        return builder.toString();
    }

}
