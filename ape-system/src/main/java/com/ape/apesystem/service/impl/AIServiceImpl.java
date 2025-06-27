package com.ape.apesystem.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.ape.apecommon.domain.Result;
import com.ape.apesystem.domain.*;
import com.ape.apesystem.mapper.ApeTaskFavorMapper;
import com.ape.apesystem.mapper.ApeTaskMapper;
import com.ape.apesystem.mapper.ApeTaskStudentMapper;
import com.ape.apesystem.mapper.ApeTestMapper;
import com.ape.apesystem.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AIServiceImpl implements AIService {
    @Value("sk-4af0fdabe7524794bba259b69d3bdad4")
    private String apiKey;

    @Autowired
    private ApeUserService apeUserService;

    @Autowired
    private ApeTaskStudentMapper apeTaskStudentMapper;
    @Autowired
    private ApeTaskService apeTaskService;
    @Autowired
    private ApeTaskFavorMapper apeTaskFavorMapper;
    @Autowired
    private ApeTestMapper apeTestMapper;
    @Autowired
    private ApeTaskMapper apeTaskMapper;

    //AI聊天
    @Override
    public GenerationResult callWithMessage(String key) throws ApiException,NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(key)
                .build();
        GenerationParam param = GenerationParam.builder()
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .temperature(0.8f)
                .apiKey(apiKey) // 添加 API key 到参数中
                .build();
        return gen.call(param);
    }

    //AI生成学习建议
    @Override
    public GenerationResult studySuggestion(String id) throws ApiException,NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个学习建议生成助手，你要根据学生的学习历史学习情况，生成合理的学习建议安排（语气要和善）")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(studySummary(id))
                .build();
        GenerationParam param = GenerationParam.builder()
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .temperature(0.8f)
                .apiKey(apiKey) // 添加 API key 到参数中
                .build();
        return gen.call(param);
    }


    private String studySummary(String id){
        //获取用户
        ApeUser user= findUser(id);
        //获取课程
        List<ApeTaskStudent> task = getMyTask(id);
        //获取收藏
        List<ApeTaskFavor> taskFavor=getTaskFavor(id);
        String summary="我是用户"+user.getUserName()+"\n"+
                "我的专业是"+user.getMajor()+"\n"+
                "我正在学习的课程有"+task.size()+"门，分别是："+ listToTaskStr(task)+"\n"+
                "我收藏的课程有："+listToTaskFavorStr(taskFavor)+"\n"+
                isTest(task)+"\n"+
                "可供学习的课程有"+getAllTasks();
        String form="请你根据我的学习情况，给出学习建议，请在所提供的信息内给出一个星期的学习建议，不要太死板，可以灵活一点，还要涉及我的收藏课程这些信息，要全面一些" +
                "不要自由发挥(在给我建议之前，先说xxx（用我的名字代替这个xxx）同学你好)。在最后，可以推荐一下我正在学习的课程的类里面的其他课程（仅仅是我正在学习的，不包括我收藏的课程，要是我" +
                "正在学习的这个课程的所属类没有其他课程，就略过这个课程，要严格按照这个课程所属类下面的课程推荐）。";
        return summary+form;
    }

    private String getAllTasks(){
        String str="";
        List<ApeTask> tasks=apeTaskMapper.selectList(null);
        for(ApeTask task:tasks){
            str+=task.getName()+"("+task.getClassification()+")、";
        }
        str=str.substring(0,str.length()-1);
        str+="(括号内是课程所属的类)";
        return str;
    }



    private String isTest(List<ApeTaskStudent> task){
        String str="";
        int count=0;

        for(ApeTaskStudent taskStudent:task){
            QueryWrapper<ApeTest> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_name",taskStudent.getTaskName());
            List<ApeTest> apeTests=apeTestMapper.selectList(queryWrapper);
            if(!apeTests.isEmpty()&&(apeTests.get(0).getEndTime().compareTo(new Date())>0)){
                str+=apeTests.get(0).getTaskName()+"(截止时间是)"+apeTests.get(0).getEndTime()+",";
                count++;
            }
        }
        if(count>0){
            return "我还有"+count+"门考试没完成，分别是："+str;
        }
        else {
            return "";
        }
    }

    private ApeUser findUser(String id){
        ApeUser apeUser = apeUserService.getById(id);
        return apeUser;
    }

    private List<ApeTaskStudent> getMyTask(String id){
        QueryWrapper<ApeTaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<ApeTaskStudent> apeTaskStudents=apeTaskStudentMapper.selectList(queryWrapper);
        for(ApeTaskStudent taskStudent:apeTaskStudents){
            ApeTask task = apeTaskService.getById(taskStudent.getTaskId());
            taskStudent.setTaskDescribe(task.getTaskDescribe());
            taskStudent.setNum(task.getNum());
            taskStudent.setImage(task.getImage());
            taskStudent.setClassification(task.getClassification());
        }
        return apeTaskStudents;
    }

    private List<ApeTaskFavor> getTaskFavor(String id){
        QueryWrapper<ApeTaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<ApeTaskFavor> apeTaskFavors=apeTaskFavorMapper.selectList(queryWrapper);
        return apeTaskFavors;
    }

    private String listToTaskFavorStr(List<ApeTaskFavor> list){
        String str="";
        for(int i=0;i<list.size();i++){
            str+=list.get(i).getTaskName()+"、";
        }
        return str;
    }

    private String listToTaskStr(List<ApeTaskStudent> list){
        String str="";
        for(int i=0;i<list.size();i++){
            str+=list.get(i).getTaskName()+",它的课程描述是"+list.get(i).getTaskDescribe()+
                    "对应的类是"+list.get(i).getClassification()+"。";
        }
        return str;
    }
}
