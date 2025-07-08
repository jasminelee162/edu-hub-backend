package com.project.admin.controller.user;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.common.annotation.Log;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.enums.ResultCode;
import com.project.common.utils.PasswordUtils;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.Role;
import com.project.system.domain.User;
import com.project.system.domain.UserDocument;
import com.project.system.domain.UserRole;
import com.project.system.mapper.UserMapper;
import com.project.system.service.RoleService;
import com.project.system.service.UserRoleService;
import com.project.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @description: 用户controller
 */
@Controller
@ResponseBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserMapper userMapper;

    /** 分页查询用户 */
    @Log(name = "分页查询用户", type = BusinessType.OTHER)
    @PostMapping("getUserPage")
    public Result getUserPage(@RequestBody User user) {
        Page<User> page = userService.getUserPage(user);
        return Result.success(page);
    }

    /** 根据类型查询用户 */
    @Log(name = "根据类型查询用户", type = BusinessType.OTHER)
    @GetMapping("getUserListByType")
    public Result getUserListByType(@RequestParam("type")Integer type) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserType,type);
        List<User> userList = userService.list(queryWrapper);
        return Result.success(userList);
    }

    /** 根据id查询用户 */
    @Log(name = "根据id查询用户", type = BusinessType.OTHER)
    @GetMapping("getUserById")
    public Result getUserById(@RequestParam("id") String id) {
        User user = userService.getById(id);
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRole::getUserId, user.getId());
        List<UserRole> list = userRoleService.list(queryWrapper);
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : list) {
            roles.add(userRole.getRoleId());
        }
        user.setRoleIds(roles);
        return Result.success(user);
    }

    /** 新增用户 */
    @Log(name = "新增用户", type = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("saveUser")
    public Result saveUser(@RequestBody User user) {
        //先校验登陆账号是否重复
        boolean account = checkAccount(user);
        if (!account) {
            return Result.fail("登陆账号已存在不可重复！");
        }
        String uuid = IdWorker.get32UUID();
        //密码加盐加密
        String encrypt = PasswordUtils.encrypt(user.getPassword());
        String[] split = encrypt.split("\\$");
        user.setId(uuid);
        user.setPassword(split[0]);
        user.setSalt(split[1]);
        user.setAvatar("/img/avatar.jpg");
        user.setPwdUpdateDate(new Date());

        //保存用户
        boolean save = userService.save(user);
        //再保存用户角色关系
        List<String> roleIds = user.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();
        if (roleIds != null && roleIds.size() > 0) {
            for (String roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(uuid);
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
        }
        if (user.getUserType() == 1) {
            UserRole userRole = new UserRole();
            userRole.setUserId(uuid);
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Role::getRoleKey,"teacher");
            Role role = roleService.getOne(queryWrapper);
            userRole.setRoleId(role.getId());
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles);
        return Result.success();
    }

    /** 编辑用户 */
    @Log(name = "编辑用户", type = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("editUser")
    public Result editUser(@RequestBody User apeUser) {
        User user = userService.getById(apeUser.getId());
        if (!user.getLoginAccount().equals(apeUser.getLoginAccount())) {
            //先校验登陆账号是否重复
            boolean account = checkAccount(apeUser);
            if (!account) {
                return Result.fail("登陆账号已存在不可重复！");
            }
        }
        //更新用户
        boolean edit = userService.updateById(apeUser);
        //先删除用户角色关系
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRole::getUserId,apeUser.getId());
        userRoleService.remove(queryWrapper);
        //再次保存最新的关系
        List<String> roleIds = apeUser.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();
        if (roleIds != null && roleIds.size() > 0) {
            for (String roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(apeUser.getId());
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
        }
        if (apeUser.getUserType() == 1) {
            UserRole userRole = new UserRole();
            userRole.setUserId(apeUser.getId());
            QueryWrapper<Role> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(Role::getRoleKey,"teacher");
            Role role = roleService.getOne(queryWrapper1);
            userRole.setRoleId(role.getId());
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles);
        return Result.success();
    }

    /** 校验用户 */
    public boolean checkAccount(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getLoginAccount, user.getLoginAccount());
        int count = userService.count(queryWrapper);
        return count <= 0;
    }

    /** 删除用户 */
    @Log(name = "删除用户", type = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("removeUser")
    public Result removeUser(@RequestParam("ids")String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] asList = ids.split(",");
            for (String id : asList) {
                boolean remove = userService.removeById(id);
                QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(UserRole::getUserId,id);
                userRoleService.remove(queryWrapper);
            }
            return Result.success();
        } else {
            return Result.fail("角色id不能为空！");
        }
    }

    @Log(name = "修改密码", type = BusinessType.UPDATE)
    @PostMapping("changePassword")
    public Result changePassword(@RequestBody JSONObject json) {
        String id = json.getString("id");
        String password = json.getString("password");
        User user = userService.getById(id);
        boolean decrypt = PasswordUtils.decrypt(password, user.getPassword() + "$" + user.getSalt());
        if (decrypt) {
            String newPassword = json.getString("newPassword");
            String encrypt = PasswordUtils.encrypt(newPassword);
            String[] split = encrypt.split("\\$");
            user.setSalt(split[1]);
            user.setPassword(split[0]);
            user.setPwdUpdateDate(new Date());
            boolean update = userService.updateById(user);
            if (update) {
                return Result.success();
            } else {
                return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
            }
        } else {
            return Result.fail("旧密码不正确");
        }
    }

    /** 重置密码 */
    @Log(name = "重置密码", type = BusinessType.UPDATE)
    @PostMapping("resetPassword")
    public Result resetPassword(@RequestBody JSONObject json) {
        String id = json.getString("id");
        String newPassword = json.getString("newPassword");
        String encrypt = PasswordUtils.encrypt(newPassword);
        String[] split = encrypt.split("\\$");
        User user = userService.getById(id);
        boolean decrypt = PasswordUtils.decrypt(newPassword, user.getPassword() + "$" + user.getSalt());
        if (decrypt) {
            return Result.fail("新密码不可和旧密码相同！");
        }
        user.setPassword(split[0]);
        user.setSalt(split[1]);
        user.setPwdUpdateDate(new Date());
        boolean update = userService.updateById(user);
        if (update) {
            return Result.success();
        } else {
            return Result.fail(ResultCode.COMMON_DATA_OPTION_ERROR.getMessage());
        }
    }

    /** 获取登陆用户信息 */
    @Log(name = "获取登陆用户信息", type = BusinessType.OTHER)
    @GetMapping("getUserInfo")
    public Result getUserInfo() {
        User user = ShiroUtils.getUserInfo();
        User apeUser = userService.getById(user.getId());
        return Result.success(apeUser);
    }

    /** 修改个人信息 */
    @Log(name = "修改个人信息", type = BusinessType.UPDATE)
    @PostMapping("setUserInfo")
    public Result setUserInfo(@RequestBody User user) {
        User userInfo = ShiroUtils.getUserInfo();
        user.setId(userInfo.getId());
        userService.updateById(user);
        return Result.success();
    }

    /** 修改个人头像 */
    @Log(name = "修改个人头像", type = BusinessType.UPDATE)
    @PostMapping("setUserAvatar/{id}")
    public Result setUserAvatar(@PathVariable("id") String id,@RequestParam("file") MultipartFile avatar) {
        if(StringUtils.isBlank(id)){
            return Result.fail("用户id为空!");
        }
        User user = userService.getById(id);
        if(avatar.isEmpty()){
            return Result.fail("上传的头像不能为空!");
        }
        String coverType = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if ("jpeg".equals(coverType)  || "gif".equals(coverType) || "png".equals(coverType) || "bmp".equals(coverType)  || "jpg".equals(coverType)) {
            //文件路径
            String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img";
            //文件名=当前时间到毫秒+原来的文件名
            String fileName = id + "."+ coverType;
            //如果文件路径不存在，新增该路径
            File file1 = new File(filePath);
            if(!file1.exists()){
                boolean mkdir = file1.mkdir();
            }
            //现在的文件地址
            if (StringUtils.isNotBlank(user.getAvatar())) {
                String s = user.getAvatar().split("/")[2];
                File now = new File(filePath + System.getProperty("file.separator") + s);
                boolean delete = now.delete();
            }
            //实际的文件地址
            File dest = new File(filePath + System.getProperty("file.separator") + fileName);
            //存储到数据库里的相对文件地址
            String storeImgPath = "/img/"+fileName;
            try {
                avatar.transferTo(dest);
                //更新头像
                user.setAvatar(storeImgPath);
                userService.updateById(user);
                return Result.success(storeImgPath);
            } catch (IOException e) {
                return Result.fail("上传失败");
            }
        } else {
            return Result.fail("请选择正确的图片格式");
        }
    }


    //获取是否有未审核的入驻教师申请
    @GetMapping("/unread")
    public Result unread() {
        return Result.success(userService.unRead());
    }

    //点击后表示已读（红点取消）
    @PostMapping("/checked")
    public Result checked(@RequestParam String userName){
        userService.checked(userName);
        userService.sendEmail(userName);
        return Result.success();
    }

}
