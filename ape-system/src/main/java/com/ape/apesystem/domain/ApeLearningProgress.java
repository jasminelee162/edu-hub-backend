package com.ape.apesystem.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ape_learning_progress")
public class ApeLearningProgress {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    private String userId;
    private String userName; // 学生姓名
    private int progress;
    private Date createTime;
    private Date updateTime;
    private String teacherName; // 教师姓名
    private String taskName; // 课程名称
}
