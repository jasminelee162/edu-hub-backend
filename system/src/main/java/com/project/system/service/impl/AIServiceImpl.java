package com.project.system.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.project.system.domain.*;
import com.project.system.mapper.TaskFavorMapper;
import com.project.system.mapper.TaskMapper;
import com.project.system.mapper.TaskStudentMapper;
import com.project.system.mapper.TestMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.system.service.AIService;
import com.project.system.service.TaskService;
import com.project.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AIServiceImpl implements AIService {
    @Value("sk-4af0fdabe7524794bba259b69d3bdad4")
    private String apiKey;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskStudentMapper taskStudentMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskFavorMapper taskFavorMapper;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private TaskMapper taskMapper;

    private static String introduction ="有两点重要内容：1、你是我的项目平台的AI助手，如果让你介绍这个平台你可以回答：欢迎使用智能化在线教学支持服务平台！作为你的专属 AI 助手，我很乐意为你介绍这个集 “学、练、做、评” 于一体的数字化学习空间。\n" +
            "\n" +
            "在这里，你可以自由探索多学科虚拟仿真实验中心 —— 从基础操作到复杂协作项目，每一步实验操作都会被精准记录，支持回放复盘，还能自动生成报告模板，让实验学习既高效又省心。课程学习更轻松，你能随时获取课件、视频等资源，系统会实时追踪你的学习进度，标记已学内容，还能通过互动社区和同学老师交流讨论，不错过任何课程更新和作业提醒。\n" +
            "\n" +
            "作业与考试系统也十分贴心，无论是主观题还是客观题，都能在线完成提交；考试时支持限时答题和智能评分，错题解析和教师反馈一目了然，还能根据你的情况安排二次提交。更重要的是，平台会基于你的学习数据，为你量身定制学习路径，推荐合适的课程和资料，用可视化图表展示你的优势与薄弱环节，AI 还会给出针对性的提升建议。\n" +
            "\n" +
            "安全方面，我们通过多角色权限控制、动态权限调整和多因素认证保障你的账号安全，每一次操作都会被详细记录，确保学习过程合规可追溯。\n" +
            "\n" +
            "无论你是想巩固知识、完成实验，还是备考提升，这个平台都能为你提供全方位的智能化支持，让学习更高效、更个性化！"+
            "2、要是让你自我介绍，你要回答：你好！我是智能化在线教学支持服务平台的AI助手，有问题想问我或者跟我聊天都可以呀！" +
            "3、其他内容可以自由发挥" +
            "用户这次提的问题是：";

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
                .content(introduction+key)
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
        User user= findUser(id);
        //获取课程
        List<TaskStudent> task = getMyTask(id);
        //获取收藏
        List<TaskFavor> taskFavor=getTaskFavor(id);
        String summary="我是用户"+user.getUserName()+"\n"+
                "我的专业是"+user.getMajor()+"\n"+
                "我正在学习的课程有"+task.size()+"门，分别是："+ listToTaskStr(task)+"\n"+
                "我收藏的课程有："+listToTaskFavorStr(taskFavor)+"\n"+
                isTest(task)+"\n"+
                "可供学习的课程有"+getAllTasks();
        String form="请你根据我的学习情况，给出学习建议，请在所提供的信息内给出一个星期的学习建议，不要太死板，可以灵活一点，还要涉及我的收藏课程这些信息，要全面一些" +
                "不要自由发挥(在给我建议之前，先说xxx（用我的名字代替这个xxx）同学你好)。在最后，可以推荐一下我正在学习的课程的类里面的其他课程（仅仅是我正在学习的，不包括我收藏的课程，要是我" +
                "正在学习的这个课程的所属类没有其他课程，就略过这个课程，要严格按照这个课程所属类下面的课程推荐）；如果这个用户既没有正在学习的课程也没有收藏的课程，就可以给他推荐一下课程；这些课程是平台具有的，不是用户列出来的";
        return summary+form;
    }

    private String getAllTasks(){
        String str="";
        List<Task> tasks= taskMapper.selectList(null);
        for(Task task:tasks){
            str+=task.getName()+"("+task.getClassification()+")、";
        }
        str=str.substring(0,str.length()-1);
        str+="(括号内是课程所属的类)";
        return str;
    }



    private String isTest(List<TaskStudent> task){
        String str="";
        int count=0;

        for(TaskStudent taskStudent:task){
            QueryWrapper<Test> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_name",taskStudent.getTaskName());
            List<Test> tests = testMapper.selectList(queryWrapper);
            if(!tests.isEmpty()&&(tests.get(0).getEndTime().compareTo(new Date())>0)){
                str+= tests.get(0).getTaskName()+"(截止时间是)"+ tests.get(0).getEndTime()+",";
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

    private User findUser(String id){
        User user = userService.getById(id);
        return user;
    }

    private List<TaskStudent> getMyTask(String id){
        QueryWrapper<TaskStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<TaskStudent> taskStudents = taskStudentMapper.selectList(queryWrapper);
        for(TaskStudent taskStudent: taskStudents){
            Task task = taskService.getById(taskStudent.getTaskId());
            taskStudent.setTaskDescribe(task.getTaskDescribe());
            taskStudent.setNum(task.getNum());
            taskStudent.setImage(task.getImage());
            taskStudent.setClassification(task.getClassification());
        }
        return taskStudents;
    }

    private List<TaskFavor> getTaskFavor(String id){
        QueryWrapper<TaskFavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<TaskFavor> taskFavors = taskFavorMapper.selectList(queryWrapper);
        return taskFavors;
    }

    private String listToTaskFavorStr(List<TaskFavor> list){
        String str="";
        for(int i=0;i<list.size();i++){
            str+=list.get(i).getTaskName()+"、";
        }
        return str;
    }

    private String listToTaskStr(List<TaskStudent> list){
        String str="";
        for(int i=0;i<list.size();i++){
            str+=list.get(i).getTaskName()+",它的课程描述是"+list.get(i).getTaskDescribe()+
                    "对应的类是"+list.get(i).getClassification()+"。";
        }
        return str;
    }
}
