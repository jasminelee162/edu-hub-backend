package com.project.system.dto;

import com.project.system.domain.GenTable;
import com.project.system.domain.GenTableColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 生成字典编辑DTO
 * @date 2023/10/12 15:43
 */
@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GenTableColumnEditDto {

    private GenTable genTable;

    private List<GenTableColumn> columns;

}
