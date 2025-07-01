package com.project.generator;

import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @version 1.0
 * @description: mapper接口代码生成类
 * 15:36
 */
public class GenMapperCode {

    public static String genMapper(GenTable genTable, List<GenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.mapper;\n" + "\n" + "import com.ape.apesystem.domain.").append(genTable.getClassName()).append(";\n").append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(genTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(genTable.getBusinessName()).append("mapper\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("public interface ").append(genTable.getClassName()).append("Mapper extends BaseMapper<").append(genTable.getClassName()).append("> {\n").append("}");
        return builder.toString();
    }

    public static  String genMapperXml(GenTable genTable, List<GenTableColumn> columnList) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<!DOCTYPE mapper\n" + "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" + "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" + "<mapper namespace=\"com.ape.apesystem.mapper." + genTable.getClassName() + "Mapper\">\n" + "\n" + "</mapper>";
    }

}
