package com.project.generator;

import com.project.common.utils.HumpUtils;
import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: domain代码生成类
 * @date 2023/10/13 9:25
 */
public class GenDomainCode {

    public static String genDomain(GenTable genTable, List<GenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        StringBuilder time = new StringBuilder();
        builder.append("package com.ape.apesystem.domain;\n\n");
        builder.append("import domain.com.project.common.BaseEntity;\n" +
                "import com.baomidou.mybatisplus.annotation.*;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Data;\n" +
                "import java.util.Date;\n" +
                "import java.io.Serializable;\n" +
                "import lombok.EqualsAndHashCode;\n" +
                "import lombok.NoArgsConstructor;\n" +
                "import lombok.experimental.Accessors;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(genTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(genTable.getBusinessName()).append("\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("@Data\n" + "@AllArgsConstructor\n" + "@NoArgsConstructor\n" + "@Accessors(chain = true)\n" + "@TableName(\"").append(genTable.getTableName()).append("\")\n");
        builder.append("public class ").append(genTable.getClassName()).append(" implements Serializable {\n\n");
        builder.append("     private static final long serialVersionUID = 1L;\n\n");
        for (GenTableColumn genTableColumn : columnList) {
            String s = HumpUtils.toCamel(genTableColumn.getJavaField(), "_");
            String sm = HumpUtils.toSmallCamel(genTableColumn.getJavaField(), "_");
            if (genTableColumn.getIsPk() == 1) {
                builder.append("     @TableId(value = \"").append(genTableColumn.getColumnName()).append("\", type = IdType.ASSIGN_ID)\n");
            } else {
                builder.append("     /**\n" + "     * ").append(genTableColumn.getColumnComment()).append("\n").append("     */\n");
            }
            if ("Date".equals(genTableColumn.getJavaType())) {
                builder.append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n" +
                        "    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n");
            }
            if ("createBy".equals(genTableColumn.getJavaField())) {
                builder.append("    @TableField(value = \"create_by\", fill = FieldFill.INSERT)\n");
            }
            if ("updateBy".equals(genTableColumn.getJavaField())) {
                builder.append("    @TableField(value = \"update_by\", fill = FieldFill.INSERT_UPDATE)\n");
            }
            if ("createTime".equals(genTableColumn.getJavaField())) {
                builder.append("    @TableField(value = \"create_time\", fill = FieldFill.INSERT)\n");
            }
            if ("updateTime".equals(genTableColumn.getJavaField())) {
                builder.append("    @TableField(value = \"update_time\", fill = FieldFill.INSERT_UPDATE)\n");
            }
            builder.append("     private ").append(genTableColumn.getJavaType()).append(" ").append(genTableColumn.getJavaField()).append(";\n\n");
            if (genTableColumn.getIsQuery() == 1) {
                if ("BETWEEN".equals(genTableColumn.getQueryType()) && "Date".equals(genTableColumn.getJavaType())) {
                    time.append("     @TableField(exist = false)\n" + "    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n" + "    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n" + "    private Date ").append(sm).append("StartTime;\n").append("\n").append("    @TableField(exist = false)\n").append("    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n").append("    @JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n").append("    private Date ").append(sm).append("EndTime;\n\n");
                }
            }
        }
        builder.append(time);
        builder.append("     @TableField(exist = false)\n" +
                "     private Integer pageNumber;\n" +
                "\n" +
                "     @TableField(exist = false)\n" +
                "     private Integer pageSize;\n");
        builder.append("}");
        return builder.toString();
    }

}
