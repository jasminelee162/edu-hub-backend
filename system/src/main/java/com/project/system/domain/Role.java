package com.project.system.domain;

import com.project.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *  
 * @version 1.0
 * @description: 角色
 *  2023/8/31 10:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限标识
     */
    private String roleKey;

    /**
     * 顺序
     */
    private Integer roleSort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 删除标志
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    List<String> menuIds;

}
