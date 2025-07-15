# Edu Hub 后端

Edu Hub 是一套面向学生、教师和管理员的智能教育平台，提供从课程学习、作业考试到协作交流、数据分析的一站式解决方案。本项目为 Edu Hub 的后端服务，负责支撑前端所有功能模块的数据与业务逻辑。

- 学生端前端：[edu-hub-student-frontend](https://github.com/jasminelee162/edu-hub-student-frontend)
- 管理端前端：[edu-hub-admin-frontend](https://github.com/jasminelee162/edu-hub-admin-frontend)
- 后端服务（本仓库）：[edu-hub-backend](https://github.com/jasminelee162/edu-hub-backend)

---

## 功能模块

### 1. 用户登录注册
- 用户名/密码登录
- 邮箱验证码登录

### 2. 课程中心
- 课程报名（需管理员审核）
- 课程答疑与教师答复
- 章节作业（按课程章节布置与提交）
- 课件下载
- 课程学习进度追踪
- 课程评论
- 课程笔记  
  - 记笔记（需管理员审核）
- 我的错题集、收藏课程

### 3. 教师管理
- 教师信息与课程关联

### 4. 实验功能
- 数据库实验
- 多学科实验支持

### 5. 考试系统
- 在线考试（需管理员审核）
- 自动与人工阅卷

### 6. 共享协作文档
- 多人协作编辑
- 历史版本回滚

### 7. 讨论区
- 教师发布讨论主题
- 学生参与讨论

### 8. 公告系统
- 管理员发布平台公告

### 9. 留言交流
- 学生与管理员留言、回复

### 10. 个人中心
- 修改头像
- 编辑个人信息

### 11. 学科分析与AI助学
- 弱势与优势科目分析
- AI学习建议与助学（流式数据推送）

### 12. 学业总览
- 我的作业、课程、笔记、错题、收藏等统一管理

---

## 管理端（管理员功能）

1. 首页数据看板  
   - 用户数、课程总览、学生选课、性别比例等可视化
2. 学校与专业管理
3. 留言管理  
   - 回复学生留言
4. 课程管理  
   - 学生课程申请审核
   - 课程及章节管理
5. 笔记管理  
   - 笔记审核
6. 讨论管理
7. 考试管理  
   - 考试申请审核
   - AI阅卷与人工批改
   - 考试信息维护
8. 用户管理  
   - 教师/学生审核与管理（审核通过/不通过发送邮件）
9. 通知公告管理
10. 个人中心
11. 系统管理  
    - 角色、菜单权限管理
12. 日志管理  
    - 登录、操作日志
13. 消息通知  
    - 红点机制（未读消息提醒）

---

## 教师端功能

1. 首页  
   - 教学进度、学生成绩分析、课程资源总览
2. 课程管理  
   - 管理本人课程与章节
   - 发布章节作业
3. 学生笔记管理  
   - 笔记审核与评论
4. 课程答疑  
   - 回复学生提问
5. 课程考试管理  
   - 试卷布置与批改（同管理员权限）
6. 讨论管理  
   - 发布与管理讨论主题
7. 个人中心

---

## 技术栈

- **主语言**：Node.js / TypeScript（如为Java/Python按实际填写）
- **Web框架**：Express / NestJS / Spring Boot / Django
- **数据库**：MySQL / PostgreSQL / MongoDB
- **缓存**：Redis
- **消息队列**：选配，如 RabbitMQ、Kafka
- **认证**：JWT
- **实时通信**：WebSocket/Socket.io（用于协作编辑、AI流式推送等）

---

## 快速开始

1. 克隆代码  
   ```bash
   git clone https://github.com/jasminelee162/edu-hub-backend.git
   cd edu-hub-backend
   ```
2. 安装依赖  
   ```bash
   npm install
   # 或 yarn install
   ```
3. 配置数据库和环境变量  
   - 复制 `.env.example` 为 `.env`，按需填写数据库、Redis等配置
4. 启动开发环境  
   ```bash
   npm run dev
   ```
5. 启动生产环境  
   ```bash
   npm run start
   ```

---

## API 文档

- 提供 RESTful API，建议通过 Swagger 或 Postman 获取接口文档（如已集成，访问 `/api-docs`）。

---

## 参与贡献

欢迎提交 Issue 和 PR 参与开发。在提交前建议阅读 [CONTRIBUTING.md](CONTRIBUTING.md)（如有）。

---

## License

MIT © jasminelee162

---
