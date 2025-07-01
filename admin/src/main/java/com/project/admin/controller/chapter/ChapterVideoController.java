package com.project.admin.controller.chapter;

import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.ChapterVideo;
import com.project.system.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.system.service.ChapterVideoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author 超级管理员
 * @version 1.0
 * @description: 章节视频是否观看controller
 * @date 2023/11/23 10:39
 */
@Controller
@ResponseBody
@RequestMapping("chapterVideo")
public class ChapterVideoController {

    @Autowired
    private ChapterVideoService chapterVideoService;

    /** 分页获取章节视频是否观看 */
    @Log(name = "分页获取章节视频是否观看", type = BusinessType.OTHER)
    @PostMapping("getChapterVideoPage")
    public Result getChapterVideoPage(@RequestBody ChapterVideo chapterVideo) {
        Page<ChapterVideo> page = new Page<>(chapterVideo.getPageNumber(), chapterVideo.getPageSize());
        QueryWrapper<ChapterVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(chapterVideo.getChapterId()), ChapterVideo::getChapterId, chapterVideo.getChapterId())
                .eq(StringUtils.isNotBlank(chapterVideo.getUserId()), ChapterVideo::getUserId, chapterVideo.getUserId());
        Page<ChapterVideo> chapterVideoPage = chapterVideoService.page(page, queryWrapper);
        return Result.success(chapterVideoPage);
    }

    /** 根据id获取章节视频是否观看 */
    @Log(name = "根据id获取章节视频是否观看", type = BusinessType.OTHER)
    @GetMapping("getChapterVideoById")
    public Result getChapterVideoById(@RequestParam("id")String id) {
        ChapterVideo chapterVideo = chapterVideoService.getById(id);
        return Result.success(chapterVideo);
    }

    /** 保存章节视频是否观看 */
    @Log(name = "保存章节视频是否观看", type = BusinessType.INSERT)
    @PostMapping("saveChapterVideo")
    public Result saveChapterVideo(@RequestBody ChapterVideo chapterVideo) {
        User userInfo = ShiroUtils.getUserInfo();
        QueryWrapper<ChapterVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChapterVideo::getChapterId, chapterVideo.getChapterId())
                        .eq(ChapterVideo::getUserId,userInfo.getId());
        int count = chapterVideoService.count(queryWrapper);
        if (count <= 0) {
            chapterVideo.setUserId(userInfo.getId());
            boolean save = chapterVideoService.save(chapterVideo);
            if (save) {

            } else {
                return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
            }
        }
        return Result.success();
    }

    /** 编辑章节视频是否观看 */
    @Log(name = "编辑章节视频是否观看", type = BusinessType.UPDATE)
    @PostMapping("editChapterVideo")
    public Result editChapterVideo(@RequestBody ChapterVideo chapterVideo) {
        boolean save = chapterVideoService.updateById(chapterVideo);
        if (save) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 删除章节视频是否观看 */
    @GetMapping("removeChapterVideo")
    @Log(name = "删除章节视频是否观看", type = BusinessType.DELETE)
    public Result removeChapterVideo(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                chapterVideoService.removeById(id);
            }
            return Result.success();
        } else {
            return Result.fail("章节视频是否观看id不能为空！");
        }
    }

}