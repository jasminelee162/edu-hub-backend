package com.project.system.domain;



import com.project.common.enums.DifficultyLevel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@TableName("exp_question")
public class ExpQuestion {
    @Id
    private String id;
    private String title;
    private String description;
    private DifficultyLevel difficulty;
    @TableField("standard_answer")
    private String standardAnswer;
    @TableField("table_schemas")
    private String tableSchemas;
    @TableField(
            fill = FieldFill.INSERT
    )
    private LocalDateTime createTime;
    @TableField("data_script")
    private String dataScript;
}