
# zzcoder OJ 平台

zzcoder 是一个在线判题（OJ）平台，支持编程题目的提交、自动评测和竞赛管理，适用于算法学习、自主训练和在线竞赛环境。

## 🌟 项目亮点

- 🧾 支持题目管理、题单管理、竞赛管理等核心模块
- 🔐 使用 Shiro + JWT + RBAC 实现登录认证与权限控制
- 🚀 判题服务基于 Docker 隔离执行，保证安全性和公平性
- 🕓 使用线程池实现异步判题任务，提高系统吞吐量
- 🛠️ RabbitMQ 实现后端服务与判题机间通信

- ⏰ 定时任务自动更新比赛状态


## 🛠 技术栈

| 模块       | 技术               |
|------------|--------------------|
| 后端       | Spring Boot, MyBatis|
| 安全       | Apache Shiro, JWT, RBAC |
| 消息队列   | RabbitMQ           |
| 缓存       | Redis              |
| 判题沙箱   | Docker             |
| 数据库     | MySQL   |
| 前端（可选）| Vue.js|

## 📂 模块说明

- `zzcoder-service`：后端接口服务，提供题目、用户、竞赛等 API
- `zzcoder-judge`：判题服务，接收消息队列任务并运行代码
- `zzcoder-common`：通用工具、常量、统一返回结构等

## 🚀 快速启动

1. **克隆项目**
   ```bash
   git clone https://github.com/zazhiii/zzcoder.git
   cd zzcoder
   ```

2. **准备环境**
   - 安装并配置 MySQL 数据库
   - 启动 Redis、RabbitMQ、Docker 环境

3. **配置数据库与 Redis**
   修改 `application.yml`，填入数据库、Redis、RabbitMQ 等配置信息。

4. **初始化数据库**
   导入 SQL 文件创建所需表结构，或使用项目中提供的初始化脚本。

5. **启动后端服务**


6. **运行判题服务**
   判题服务支持通过 RabbitMQ 接收任务并在 Docker 容器中运行代码。

## 📌 TODO

- 题解与讨论模块
- 判题结果统计图表
- 前端 UI 优化
- 多语言支持（Python/C++/Java）
- ⚙️ Redis 缓存比赛题目及用户 token，提高性能并支持服务端控制 token 实效
- 🧪 提供题目标签、收藏、评论、点赞等交互功能

## 📄 License

MIT License © [zazhiii](https://github.com/zazhiii)
