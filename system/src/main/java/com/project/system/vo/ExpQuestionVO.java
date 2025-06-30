package com.project.system.vo;


import com.project.common.enums.DifficultyLevel;
import lombok.Data;

@Data
public class ExpQuestionVO {
    private String id;
    private String title;
    private DifficultyLevel difficulty;
}