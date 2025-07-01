package com.project.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *  超级管理员
 * @version 1.0
 * @description: 答疑
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 提问内容
     */
    private String content;

    /**
     * 回复
     */
    private String answer;

    /**
     * 教师id
     */
    private String teacherId;

    /**
     * 课程id
     */
    private String taskId;

    /**
     * 课程名称
     */
    private String taskName;

    @TableField(exist = false)
    private Integer pageNumber;

    @TableField(exist = false)
    private Integer pageSize;
}