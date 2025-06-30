package com.project.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("experiment")  // 数据库表名
public class Experiment {

    @TableId(type = IdType.AUTO)  // 设置主键自增
    private Integer id;

    private String label;
    private String url;
    private String subject;

}