package com.project.generator;

import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *  shaozhujie
 * @version 1.0
 * @description: TODO
 *  2023/10/13 16:35
 */
public class GenServiceCode {

    public static String genService(GenTable genTable, List<GenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.service;\n" + "\n" + "import com.ape.apesystem.domain.").append(genTable.getClassName()).append(";\n").append("import com.baomidou.mybatisplus.extension.service.IService;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(genTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(genTable.getBusinessName()).append("service\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("public interface ").append(genTable.getClassName()).append("Service extends IService<").append(genTable.getClassName()).append("> {\n").append("}");
        return builder.toString();
    }

    public static String genServiceImpl(GenTable genTable, List<GenTableColumn> columnList) {
        StringBuilder builder = new StringBuilder();
        builder.append("package com.ape.apesystem.service.impl;\n" + "\n" + "import com.ape.apesystem.domain.").append(genTable.getClassName()).append(";\n").append("import com.ape.apesystem.mapper.").append(genTable.getClassName()).append("Mapper;\n").append("import com.ape.apesystem.service.").append(genTable.getClassName()).append("Service;\n").append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n").append("import org.springframework.stereotype.Service;\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String format = sdf.format(new Date());
        builder.append("/**\n" + " * @author ").append(genTable.getFunctionAuthor()).append("\n").append(" * @version 1.0\n").append(" * @description: ").append(genTable.getBusinessName()).append("service实现类\n").append(" * @date ").append(format).append("\n").append(" */\n");
        builder.append("@Service\n" +
                "public class "+ genTable.getClassName() +"ServiceImpl extends ServiceImpl<"+ genTable.getClassName() +"Mapper, "+ genTable.getClassName() +"> implements "+ genTable.getClassName() +"Service {\n" +
                "}");
        return builder.toString();
    }
}
