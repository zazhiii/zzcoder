
# zzcoder OJ 平台

zzcoder 是一个在线判题（OJ）平台，支持编程题目的提交、自动评测和竞赛管理，适用于算法学习、自主训练和在线竞赛环境。

## 🛠 技术栈

| 模块       | 技术               |
|------------|--------------------|
| 后端       | Spring Boot, MyBatis-Plus|
| 安全       | Spring Security, JWT, RBAC |
| 消息队列   | RabbitMQ           |
| 缓存       | Redis              |
| 判题沙箱   | Docker             |
| 数据库     | MySQL   |
| 前端| Vue.js|

## 📂 模块说明

- `zzcoder-api`：后端接口服务，提供题目、用户、竞赛等 API
- `zzcoder-judge`：判题服务，接收消息队列任务并运行代码

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

