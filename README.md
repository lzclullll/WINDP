# 可再生能源发电功率预测与运维平台

这是一个面向个人项目实训的前后端分离 Web 应用。正式用户界面位于 `energy-platform-frontend`，后端位于 `energy-platform-backend`。后端目录中的 `resources/static` 仅保留为早期原型参考，不作为当前交付界面。

## 功能

- 场站、设备、用户、角色和权限管理
- 实时功率、历史数据和综合看板
- 注意力启发式功率预测原型
- 告警生成工单，工单指派、解决和关闭
- 日报、月报和年报
- 基于本地资料的运维知识助手，可接入智谱开放平台
- 登录日志、操作日志和数据库 RBAC

## 环境

- JDK 17
- Maven 3.9 或更高版本
- Node.js 22
- npm 10 或更高版本
- 开发环境使用 H2，生产配置使用 MySQL 8

## 本地启动

后端：

```powershell
cd energy-platform-backend
mvn spring-boot:run
```

前端：

```powershell
cd energy-platform-frontend
npm ci
npm run dev
```

浏览器打开 `http://localhost:5173`。开发环境首次启动会重建 H2 演示数据库。默认账号为 `admin`，默认密码为 `123456`。Swagger 地址为 `http://localhost:8080/swagger-ui/index.html`。

如果希望只通过 IDEA 启动后端并统一使用 `http://localhost:8080`，先执行一次：

```powershell
cd energy-platform-frontend
npm run build:backend
```

随后在 IDEA 中重新启动 `PlatformApplication`。该命令会把最新版 Vue 页面部署到后端静态资源目录。

## 环境变量

| 名称 | 说明 | 必填 |
| --- | --- | --- |
| `JWT_SECRET` | JWT 签名密钥，部署时至少32字节 | 生产必填 |
| `CORS_ALLOWED_ORIGINS` | 允许访问后端的前端地址 | 否 |
| `LLM_BASE_URL` | 模型服务地址；智谱填写 `https://open.bigmodel.cn/api/paas/v4` | 启用智谱时必填 |
| `LLM_API_KEY` | 智谱 API Key；未设置时使用本地知识库回答 | 启用智谱时必填 |
| `LLM_MODEL` | 后端使用的模型，通常无需修改 | 否 |
| `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` | MySQL 连接信息 | 生产必填 |

在 PyCharm 的后端运行配置中增加环境变量：

```text
LLM_BASE_URL=https://open.bigmodel.cn/api/paas/v4;LLM_API_KEY=你的智谱APIKey
```

重新启动后端即可启用在线回答。启用后，当前问题和检索到的运维资料片段会发送给所配置的模型服务；API Key 不要写进代码或提交到仓库。

## 构建与测试

```powershell
cd energy-platform-backend
mvn test

cd ..\energy-platform-frontend
npm ci
npm run typecheck
npm run build
```

持续集成会对每次提交和拉取请求执行同样的后端测试、前端类型检查和生产构建。

## 答辩演示流程

1. 使用管理员账号登录并打开综合看板。
2. 查看场站详情和实时功率数据。
3. 在预测任务页创建任务并运行预测，在预测结果页查看曲线。
4. 在告警页将告警转为工单。
5. 打开工单详情，完成指派、解决和验收关闭。
6. 查看报表和系统操作日志。
7. 在运维知识助手中询问“预测偏差告警出现后应该检查什么”，再演示提示注入或敏感信息输入被拒绝。

## 安全说明

- 访问令牌和刷新令牌保存在 HttpOnly、SameSite Cookie 中，前端不保存明文令牌。
- 角色和权限从数据库关联表读取，业务接口使用方法级权限校验。
- 生产配置关闭 H2 Console 和 Swagger，不自动执行建表及演示数据脚本。
- 模型密钥只从环境变量读取。知识助手拒绝提示注入和敏感信息输入，并在回答中显示资料来源。

更多材料见 [docs](./docs) 目录。
