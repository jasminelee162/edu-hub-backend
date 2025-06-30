package com.project.system.service.impl;

import com.project.common.domain.Result;
import com.project.system.service.SqlSecurityService;
import java.util.Iterator;
import java.util.Set;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;

@Service
public class SqlSecurityServiceImpl implements SqlSecurityService {
    private static final Set<String> FORBIDDEN_KEYWORDS = Set.of("insert", "update", "delete", "drop", "alter", "create", "truncate", "grant", "revoke", "exec");
    private static final int MAX_QUERY_LENGTH = 1000;

    public SqlSecurityServiceImpl() {
    }

    public Result validate(String sql) {
        if (sql != null && sql.length() <= 1000) {
            String normalized = sql.toLowerCase().replaceAll("--.*", "").replaceAll("/\\*.*?\\*/", "");
            Iterator var3 = FORBIDDEN_KEYWORDS.iterator();

            while(var3.hasNext()) {
                String keyword = (String)var3.next();
                if (normalized.matches(".*\\b" + keyword + "\\b.*")) {
                    return Result.fail("检测到禁用关键词: " + keyword);
                }
            }

            try {
                Statement stmt = CCJSqlParserUtil.parse(sql);
                if (!(stmt instanceof Select)) {
                    return Result.fail("只允许SELECT查询");
                }
            } catch (JSQLParserException var5) {
                return Result.fail("sql解析错误");
            }

            return Result.success();
        } else {
            return Result.fail("查询过长");
        }
    }
}