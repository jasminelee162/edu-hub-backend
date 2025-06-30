package com.ape.apesystem.vo;


import com.ape.apecommon.enums.DifficultyLevel;
import lombok.Data;

@Data
public class ExpQuestionVO {
    private String id;
    private String title;
    private DifficultyLevel difficulty;
}