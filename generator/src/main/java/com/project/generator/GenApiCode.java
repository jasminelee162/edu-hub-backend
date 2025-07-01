package com.project.generator;

import com.project.common.utils.HumpUtils;
import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;

import java.util.List;

/**
 *  shaozhujie
 * @version 1.0
 * @description: api代码生成类
 *  2023/10/14 9:45
 */
public class GenApiCode {

    public static String genApi(GenTable genTable, List<GenTableColumn> columnList) {
        String name = genTable.getTableName().substring(genTable.getTableName().lastIndexOf("_") + 1);
        String toCamel = HumpUtils.toCamel(genTable.getTableName(), "_");
        return "//-------------------------------" + genTable.getBusinessName() + "---------------------------------------\n" +
                "//查询" + genTable.getBusinessName() + "\n" + "export const get" + genTable.getClassName() + "Page = (params) => post(\"/" + name + "/get" + toCamel + "Page\",params)\n" +
                "//根据id查询" + genTable.getBusinessName() + "\n" + "export const get" + genTable.getClassName() + "ById = (params) => get(\"/" + name + "/get" + genTable.getClassName() + "ById\",params)\n" +
                "//保存" + genTable.getBusinessName() + "\n" + "export const save" + genTable.getClassName() + " = (params) => post(\"/" + name + "/save" + genTable.getClassName() + "\",params)\n" +
                "//更新" + genTable.getBusinessName() + "\n" + "export const edit" + genTable.getClassName() + " = (params) => post(\"/" + name + "/edit" + genTable.getClassName() + "\",params)\n" +
                "//删除" + genTable.getClassName() + "\n" + "export const remove" + genTable.getClassName() + " = (params) => get(\"/" + name + "/remove" + genTable.getClassName() + "\",params)";
    }

}
