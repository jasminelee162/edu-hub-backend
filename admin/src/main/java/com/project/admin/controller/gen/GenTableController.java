package com.project.admin.controller.gen;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.utils.HumpUtils;
import com.project.common.utils.StringUtils;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;
import com.project.system.domain.User;
import com.project.system.dto.GenTableColumnEditDto;
import com.project.system.service.GenTableService;
import com.project.system.service.GenTableColumnService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.generator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 代码生成controller
 * @date 2023/10/10 9:25
 */
@Controller
@ResponseBody
@RequestMapping("genTable")
public class GenTableController {

    @Autowired
    private GenTableService genTableService;
    @Autowired
    private GenTableColumnService genTableColumnService;
    @Autowired
    private GenIndexCode genIndexCode;
    @Autowired
    private GenAddCode genAddCode;
    @Autowired
    private GenUpdateCode genUpdateCode;

    /**
     * 查询
     */
    @Log(name = "查询代码生成列表", type = BusinessType.OTHER)
    @PostMapping("getGenPage")
    public Result getGenPage(@RequestBody GenTable genTable) {
        Page<GenTable> page = new Page<>(genTable.getPageNumber(), genTable.getPageSize());
        QueryWrapper<GenTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(genTable.getTableName()), GenTable::getTableName, genTable.getTableName())
                .like(StringUtils.isNotBlank(genTable.getTableComment()), GenTable::getTableComment, genTable.getTableComment())
                .ge(genTable.getStartTime() != null, GenTable::getCreateTime, genTable.getStartTime())
                .le(genTable.getEndTime() != null, GenTable::getCreateTime, genTable.getEndTime())
                .orderByDesc(GenTable::getCreateTime);
        Page<GenTable> genTablePage = genTableService.page(page, queryWrapper);
        return Result.success(genTablePage);
    }

    /** 根据id获取代码生成 */
    @Log(name = "根据id获取代码生成", type = BusinessType.OTHER)
    @GetMapping("getGenById")
    public Result getGenById(@RequestParam("id")String id) {
        JSONObject gen = new JSONObject();
        GenTable genTable = genTableService.getById(id);
        QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GenTableColumn::getTableId,id);
        List<GenTableColumn> list = genTableColumnService.list(queryWrapper);
        gen.put("table", genTable);
        gen.put("column",list);
        return Result.success(gen);
    }

    /** 保存代码生成 */
    @Log(name = "保存代码生成", type = BusinessType.INSERT)
    @GetMapping("saveGen")
    @Transactional(rollbackFor = Exception.class)
    public Result saveGen(@RequestParam("tables")String tables) {
        String[] split = tables.split(",");
        User userInfo = ShiroUtils.getUserInfo();
        for (String table : split) {
            //先保存表
            Map<String, Object> tablesInfo = genTableService.getTablesInfo(table);
            GenTable genTable = new GenTable();
            String idStr = IdWorker.getIdStr();
            genTable.setId(idStr);
            genTable.setTableName(table);
            genTable.setTableComment(tablesInfo.get("tableComment").toString());
            genTable.setGenType(0);
            genTable.setTplCategory("crud");
            genTable.setClassName(HumpUtils.toCamel(table,"_"));
            genTable.setFunctionAuthor(userInfo.getUserName());
            genTable.setPackageName("com.ape.apeadmin");
            genTable.setModuleName("system");
            genTable.setFunctionName(tablesInfo.get("tableComment").toString());
            genTable.setBusinessName(tablesInfo.get("tableComment").toString());
            genTableService.save(genTable);
            //在获取字段保存字段
            List<Map<String,Object>> list = genTableColumnService.getTableField(table);
            for (Map<String,Object> map : list) {
                GenTableColumn genTableColumn = new GenTableColumn();
                genTableColumn.setTableId(idStr);
                genTableColumn.setColumnName(map.get("name").toString());
                genTableColumn.setColumnComment(map.get("comment").toString());
                genTableColumn.setColumnType(map.get("column").toString());
                if ("PRI".equals(map.get("key").toString())) {
                    genTableColumn.setIsPk(1);
                    genTableColumn.setIsQuery(0);
                    genTableColumn.setIsList(0);
                    genTableColumn.setIsEdit(0);
                } else {
                    genTableColumn.setIsPk(0);
                }
                if ("NO".equals(map.get("isNull").toString())) {
                    genTableColumn.setIsRequired(1);
                }
                String type = map.get("type").toString();
                if ("varchar".equals(type) || "text".equals(type) || "longtext".equals(type)) {
                    genTableColumn.setJavaType("String");
                }
                if ("int".equals(type) || "smallint".equals(type)) {
                    genTableColumn.setJavaType("Integer");
                }
                if ("bigint".equals(type)) {
                    genTableColumn.setJavaType("Long");
                }
                if ("decimal".equals(type)) {
                    genTableColumn.setJavaType("BigDecimal");
                }
                if ("float".equals(type)) {
                    genTableColumn.setJavaType("Float");
                }
                if ("double".equals(type)) {
                    genTableColumn.setJavaType("Double");
                }
                if ("tinyint".equals(type)) {
                    genTableColumn.setJavaType("Boolean");
                }
                if ("datetime".equals(type)) {
                    genTableColumn.setJavaType("Date");
                    genTableColumn.setHtmlType("日期控件");
                }
                genTableColumn.setJavaField(HumpUtils.toSmallCamel(map.get("name").toString(),"_"));
                genTableColumnService.save(genTableColumn);
            }
        }
        return Result.success();
    }

    /** 编辑 */
    @Log(name = "编辑代码生成", type = BusinessType.UPDATE)
    @PostMapping("editGen")
    @Transactional(rollbackFor = Exception.class)
    public Result editDict(@RequestBody GenTableColumnEditDto genTableColumnEditDto) {
        //更新表
        genTableService.updateById(genTableColumnEditDto.getGenTable());
        //更新字段
        genTableColumnService.updateBatchById(genTableColumnEditDto.getColumns());
        return Result.success();
    }

    /** 删除 */
    @GetMapping("removeGen")
    @Transactional(rollbackFor = Exception.class)
    @Log(name = "删除代码生成", type = BusinessType.DELETE)
    public Result removeGen(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                genTableService.removeById(id);
                QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(GenTableColumn::getTableId,id);
                genTableColumnService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("代码生成id不能为空！");
        }
    }

    /** 同步生成表和字段 */
    /*@GetMapping("syncTableAndColumns")
    @Log(name = "同步生成表和字段", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public Result syncTableAndColumns(@RequestParam("id")String id) {
        GenTable genTable = genTableService.getById(id);
        //先保存表
        Map<String, Object> tablesInfo = genTableService.getTablesInfo(genTable.getTableName());
        genTable.setTableComment(tablesInfo.get("tableComment").toString());
        genTable.setFunctionName(tablesInfo.get("tableComment").toString());
        genTable.setBusinessName(tablesInfo.get("tableComment").toString());
        genTableService.updateById(genTable);
        //在获取字段保存字段
        List<Map<String,Object>> list = genTableColumnService.getTableField(genTable.getTableName());
        //删除表字段
        QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GenTableColumn::getTableId, genTable.getId());
        genTableColumnService.remove(queryWrapper);
        //重新保存表字段
        for (Map<String,Object> map : list) {
            GenTableColumn genTableColumn = new GenTableColumn();
            genTableColumn.setTableId(genTable.getId());
            genTableColumn.setColumnType(map.get("column").toString());
            genTableColumn.setColumnName(map.get("name").toString());
            genTableColumn.setColumnComment(map.get("comment").toString());
            if ("PRI".equals(map.get("key").toString())) {
                genTableColumn.setIsPk(1);
                genTableColumn.setIsQuery(0);
                genTableColumn.setIsList(0);
                genTableColumn.setIsEdit(0);
            } else {
                genTableColumn.setIsPk(0);
            }
            if ("NO".equals(map.get("isNull").toString())) {
                genTableColumn.setIsRequired(1);
            }
            String type = map.get("type").toString();
            if ("varchar".equals(type) || "text".equals(type) || "longtext".equals(type)) {
                genTableColumn.setJavaType("String");
            }
            if ("int".equals(type) || "smallint".equals(type)) {
                genTableColumn.setJavaType("Integer");
            }
            if ("bigint".equals(type)) {
                genTableColumn.setJavaType("Long");
            }
            if ("decimal".equals(type)) {
                genTableColumn.setJavaType("BigDecimal");
            }
            if ("float".equals(type)) {
                genTableColumn.setJavaType("Float");
            }
            if ("double".equals(type)) {
                genTableColumn.setJavaType("Double");
            }
            if ("tinyint".equals(type)) {
                genTableColumn.setJavaType("Boolean");
            }
            if ("datetime".equals(type)) {
                genTableColumn.setJavaType("Date");
                genTableColumn.setHtmlType("日期控件");
            }
            genTableColumn.setJavaField(HumpUtils.toSmallCamel(map.get("name").toString(), "_"));
            genTableColumnService.save(genTableColumn);
        }
        return Result.success();
    }*/

    /**
     * 同步生成表和字段
     */
    @GetMapping("syncTableAndColumns")
    @Log(name = "同步生成表和字段", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public Result syncTableAndColumns(@RequestParam("id") String id) {
        try {
            // 1. 校验ID有效性
            if (StringUtils.isBlank(id)) {
                return Result.fail("ID不能为空");
            }

            // 2. 获取代码生成配置
            GenTable genTable = genTableService.getById(id);
            if (genTable == null) {
                return Result.fail("未找到对应的代码生成配置");
            }

            String tableName = genTable.getTableName();

            // 3. 检查表是否存在
            Map<String, Object> tablesInfo = genTableService.getTablesInfo(tableName);
            if (tablesInfo == null || tablesInfo.isEmpty()) {
                String errorMsg = String.format("表[%s]不存在于数据库中，请检查表名是否正确", tableName);
                return Result.fail(errorMsg);
            }

            // 4. 更新表信息
            genTable.setTableComment(String.valueOf(tablesInfo.get("tableComment")));
            genTable.setFunctionName(genTable.getTableComment());
            genTable.setBusinessName(genTable.getTableComment());
            boolean updateTable = genTableService.updateById(genTable);
            if (!updateTable) {
                throw new RuntimeException("更新表信息失败");
            }

            // 5. 获取表字段信息
            List<Map<String, Object>> columns = genTableColumnService.getTableField(tableName);
            if (CollectionUtils.isEmpty(columns)) {
                String warnMsg = String.format("表[%s]没有查询到任何字段信息", tableName);
                return Result.fail(warnMsg);
            }

            // 6. 先删除原有字段配置
            QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("table_id", genTable.getId());
            genTableColumnService.remove(queryWrapper);

            // 7. 重新保存字段配置
            List<GenTableColumn> columnList = new ArrayList<>();
            for (Map<String, Object> column : columns) {
                GenTableColumn genTableColumn = new GenTableColumn();
                // 设置字段基础信息
                genTableColumn.setTableId(genTable.getId());
                genTableColumn.setColumnName(String.valueOf(column.get("name")));
                genTableColumn.setColumnComment(String.valueOf(column.get("comment")));
                genTableColumn.setColumnType(String.valueOf(column.get("column")));

                // 设置字段属性（主键、必填等）
                if ("PRI".equalsIgnoreCase(String.valueOf(column.get("key")))) {
                    genTableColumn.setIsPk(1);
                    genTableColumn.setIsQuery(0);
                    genTableColumn.setIsList(0);
                    genTableColumn.setIsEdit(0);
                } else {
                    genTableColumn.setIsPk(0);
                }

                genTableColumn.setIsRequired("NO".equalsIgnoreCase(String.valueOf(column.get("isNull"))) ? 1 : 0);

                // 设置Java类型
                String type = String.valueOf(column.get("type")).toLowerCase();
                switch (type) {
                    case "varchar":
                    case "text":
                    case "longtext":
                        genTableColumn.setJavaType("String");
                        break;
                    case "int":
                    case "smallint":
                        genTableColumn.setJavaType("Integer");
                        break;
                    case "bigint":
                        genTableColumn.setJavaType("Long");
                        break;
                    case "decimal":
                        genTableColumn.setJavaType("BigDecimal");
                        break;
                    case "datetime":
                        genTableColumn.setJavaType("Date");
                        genTableColumn.setHtmlType("日期控件");
                        break;
                    case "tinyint":
                        genTableColumn.setJavaType("Boolean");
                        break;
                    default:
                        genTableColumn.setJavaType("String");
                }

                genTableColumn.setJavaField(HumpUtils.toSmallCamel(genTableColumn.getColumnName(), "_"));
                columnList.add(genTableColumn);
            }

            // 批量保存字段
            boolean saveBatch = genTableColumnService.saveBatch(columnList);
            if (!saveBatch) {
                throw new RuntimeException("保存字段信息失败");
            }

            return Result.success();
        } catch (Exception e) {
            return Result.fail("同步失败: " + e.getMessage());
        }
    }

    /** 预览 */
    @GetMapping("preview")
    @Log(name = "预览", type = BusinessType.OTHER)
    @Transactional(rollbackFor = Exception.class)
    public Result preview(@RequestParam("id")String id) {
        //获取表
        GenTable genTable = genTableService.getById(id);
        QueryWrapper<GenTableColumn> queryWrapper = new QueryWrapper<>();
        //获取字段
        queryWrapper.lambda().eq(GenTableColumn::getTableId,id);
        List<GenTableColumn> genTableColumns = genTableColumnService.list(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        //生成domain
        String domain = GenDomainCode.genDomain(genTable, genTableColumns);
        jsonObject.put("domain",domain);
        jsonObject.put("domainName", genTable.getClassName());
        //生成mapper接口
        String mapper = GenMapperCode.genMapper(genTable, genTableColumns);
        jsonObject.put("mapper",mapper);
        jsonObject.put("mapperName", genTable.getClassName() + "Mapper");
        //生成service接口
        String service = GenServiceCode.genService(genTable, genTableColumns);
        jsonObject.put("service",service);
        jsonObject.put("serviceName", genTable.getClassName() + "Service");
        //生成service实现类
        String serviceImpl = GenServiceCode.genServiceImpl(genTable, genTableColumns);
        jsonObject.put("serviceImpl",serviceImpl);
        jsonObject.put("serviceImplName", genTable.getClassName() + "ServiceImpl");
        //生成controller
        String controller = GenControllerCode.genController(genTable, genTableColumns);
        jsonObject.put("controller",controller);
        jsonObject.put("controllerName", genTable.getClassName() + "Controller");
        //生产mapper.xml
        String mapperXml = GenMapperCode.genMapperXml(genTable, genTableColumns);
        jsonObject.put("mapperXml",mapperXml);
        jsonObject.put("mapperXmlName", genTable.getClassName() + "Mapper.xml");
        //生成api
        String api = GenApiCode.genApi(genTable, genTableColumns);
        jsonObject.put("api",api);
        //生成index
        String index = genIndexCode.genIndex(genTable, genTableColumns);
        jsonObject.put("index",index);
        //生成add
        String add = genAddCode.genAdd(genTable, genTableColumns);
        jsonObject.put("add",add);
        //生成update
        String update = genUpdateCode.genUpdate(genTable, genTableColumns);
        jsonObject.put("update",update);
        return Result.success(jsonObject);
    }

    /** 获取数据库表 */
    @PostMapping("getTables")
    @Log(name = "获取数据库表", type = BusinessType.OTHER)
    public Result getTables(@RequestBody GenTable genTable) {
        Page<Map<String,Object>> page = genTableService.getTables(genTable);
        return Result.success(page);
    }

}
