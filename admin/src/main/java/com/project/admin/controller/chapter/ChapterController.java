package com.project.admin.controller.chapter;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system.domain.*;
import com.project.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节controller
 * @date 2023/11/17 07:14
 */
@Controller
@ResponseBody
@RequestMapping("chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private ChapterVideoService chapterVideoService;
    @Autowired
    private HomeworkStudentService homeworkStudentService;

    /** 分页获取章节 */
    @Log(name = "分页获取章节", type = BusinessType.OTHER)
    @PostMapping("getApeChapterPage")
    public Result getApeChapterPage(@RequestBody Chapter chapter) {
        Page<Chapter> page = new Page<>(chapter.getPageNumber(), chapter.getPageSize());
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        if (chapter.getType() == 1) {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Task::getTeacherId,ShiroUtils.getUserInfo().getId());
            List<Task> taskList = taskService.list(wrapper);
            List<String> list = new ArrayList<String>();
            for (Task task : taskList) {
                list.add(task.getId());
            }
            queryWrapper.lambda()
                    .like(StringUtils.isNotBlank(chapter.getTaskName()), Chapter::getTaskName, chapter.getTaskName())
                    .like(StringUtils.isNotBlank(chapter.getName()), Chapter::getName, chapter.getName());
            if (list.size()>0) {
                queryWrapper.lambda().in(Chapter::getTaskId,list);
            } else {
                list.add(" ");
                queryWrapper.lambda().in(Chapter::getTaskId,list);
            }
        } else {
            queryWrapper.lambda()
                    .like(StringUtils.isNotBlank(chapter.getTaskName()), Chapter::getTaskName, chapter.getTaskName())
                    .like(StringUtils.isNotBlank(chapter.getName()), Chapter::getName, chapter.getName());
        }
        Page<Chapter> apeChapterPage = chapterService.page(page, queryWrapper);
        return Result.success(apeChapterPage);
    }

    @GetMapping("getApeChapterByTaskId")
    public Result getApeChapterByTaskId(@RequestParam("id")String id) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Chapter::getTaskId,id);
        List<Chapter> chapterList = chapterService.list(queryWrapper);
        for (Chapter chapter : chapterList) {
            QueryWrapper<Homework> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(Homework::getChapterId, chapter.getId());
            int count = homeworkService.count(wrapper);
            if (count > 0) {
                chapter.setHomework(1);
            } else {
                chapter.setHomework(0);
            }
        }
        return Result.success(chapterList);
    }

    /** 根据id获取章节 */
    @Log(name = "根据id获取章节", type = BusinessType.OTHER)
    @GetMapping("getApeChapterById")
    public Result getApeChapterById(@RequestParam("id")String id) {
        Chapter chapter = chapterService.getById(id);
        return Result.success(chapter);
    }

    /** 保存章节 */
    @Log(name = "保存章节", type = BusinessType.INSERT)
    @PostMapping("saveApeChapter")
    public Result saveApeChapter(@RequestBody Chapter chapter) {
        if (StringUtils.isNotBlank(chapter.getTaskId())) {
            Task task = taskService.getById(chapter.getTaskId());
            chapter.setTaskName(task.getName());
        }
        boolean save = chapterService.save(chapter);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 编辑章节 */
    @Log(name = "编辑章节", type = BusinessType.UPDATE)
    @PostMapping("editApeChapter")
    public Result editApeChapter(@RequestBody Chapter chapter) {
        if (StringUtils.isNotBlank(chapter.getTaskId())) {
            Task task = taskService.getById(chapter.getTaskId());
            chapter.setTaskName(task.getName());
        }
        boolean save = chapterService.updateById(chapter);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除章节 */
    @GetMapping("removeApeChapter")
    @Log(name = "删除章节", type = BusinessType.DELETE)
    public Result removeApeChapter(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                chapterService.removeById(id);
                QueryWrapper<ChapterVideo> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ChapterVideo::getChapterId,id);
                chapterVideoService.remove(queryWrapper);
                QueryWrapper<Homework> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(Homework::getChapterId,id);
                homeworkService.remove(queryWrapper1);
                QueryWrapper<HomeworkStudent> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(HomeworkStudent::getChapterId,id);
                homeworkStudentService.remove(queryWrapper2);
            }
            return Result.success();
        } else {
            return Result.fail("章节id不能为空！");
        }
    }

    @GetMapping("getTaskChapterStudy")
    public Result getTaskChapterStudy(@RequestParam("id")String id) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Chapter::getTaskId,id);
        List<Chapter> chapterList = chapterService.list(queryWrapper);
        for (Chapter chapter : chapterList) {
            QueryWrapper<ChapterVideo> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ChapterVideo::getChapterId, chapter.getId())
                    .eq(ChapterVideo::getUserId,userInfo.getId());
            int count = chapterVideoService.count(wrapper);
            if (count > 0) {
                chapter.setVideoFlag("已完成");
            } else {
                chapter.setVideoFlag("未完成");
            }
            QueryWrapper<Homework> wrapper2 = new QueryWrapper<>();
            wrapper2.lambda().eq(Homework::getChapterId, chapter.getId());
            int count3 = homeworkService.count(wrapper2);
            if (count3 <= 0) {
                chapter.setHome("没作业");
            } else {
                chapter.setHomework(1);
                QueryWrapper<HomeworkStudent> wrapper1 = new QueryWrapper<>();
                wrapper1.lambda().eq(HomeworkStudent::getChapterId, chapter.getId())
                        .eq(HomeworkStudent::getUserId,userInfo.getId());
                int count1 = homeworkStudentService.count(wrapper1);
                if (count1 > 0) {
                    chapter.setHome("已完成");
                } else {
                    chapter.setHome("未完成");
                }
            }
        }
        return Result.success(chapterList);
    }

}