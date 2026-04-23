# 电子档案管理系统设计文档

## 1. 项目概述

### 1.1 项目背景
为长乐区水务投资集团有限公司综合处室管理人员开发一套电子档案管理系统，实现档案的数字化管理、安全存储和高效利用。

### 1.2 目标用户
- 长乐区水务投资集团有限公司综合处室管理人员

### 1.3 核心价值
- 简化档案管理操作流程
- 提高档案检索速度
- 防止物理损坏
- 加强访问控制
- 数据安全备份

## 2. 技术架构

### 2.1 技术栈
- **前端**：Vue2 + Ant Design Vue + Vue Router + Vuex + AntV（图表）
- **后端**：Java + SpringBoot + Spring Security + MyBatis
- **中间件**：RabbitMQ（消息队列）、Redis（缓存/会话）
- **数据库**：SQLite
- **文件存储**：服务器本地文件系统
- **部署**：前后端分离，分布式架构，内网部署

### 2.2 架构图
```
┌─────────────────────────────────────┐
│           Vue2 前端界面              │
│  Ant Design Vue + Vue Router + Vuex │
└──────────────┬──────────────────────┘
               │ RESTful API (HTTP/JSON)
┌──────────────┴──────────────────────┐
│       SpringBoot API 网关/服务       │
│   Spring Security + MyBatis         │
├─────────────┬───────────┬───────────┤
│   Redis     │ RabbitMQ  │  SQLite   │
│  (缓存/会话) │ (消息队列) │ (数据库)  │
├─────────────┴───────────┴───────────┤
│       本地文件存储系统               │
│     (档案文件物理存储位置)           │
└─────────────────────────────────────┘
```

### 2.3 部署方式
- 前端独立部署（Nginx或开发服务器）
- 后端SpringBoot独立服务
- 前后端通过RESTful API通信
- 内网访问，无需HTTPS
- Redis和RabbitMQ部署在同一服务器或独立服务器

## 3. 数据库设计

### 3.1 核心数据表

#### users（用户表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| username | TEXT | 用户名，唯一 |
| password_hash | TEXT | 密码哈希 |
| real_name | TEXT | 真实姓名 |
| department_id | INTEGER | 部门ID，外键 |
| role | TEXT | 角色：admin/dept_admin/user |
| status | INTEGER | 状态：1启用/0禁用 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### departments（部门表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| name | TEXT | 部门名称 |
| description | TEXT | 部门描述 |
| created_at | DATETIME | 创建时间 |

#### folders（文件夹表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| name | TEXT | 文件夹名称 |
| parent_id | INTEGER | 父文件夹ID |
| created_by | INTEGER | 创建人ID |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### categories（档案类别表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| name | TEXT | 类别名称 |
| code | TEXT | 类别编码 |
| parent_id | INTEGER | 父类别ID |
| description | TEXT | 描述 |

#### security_levels（密级等级表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| name | TEXT | 密级名称 |
| level_order | INTEGER | 等级排序 |

#### archives（档案表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| title | TEXT | 档案标题 |
| category_id | INTEGER | 类别ID，外键 |
| security_level_id | INTEGER | 密级ID，外键 |
| folder_id | INTEGER | 文件夹ID，外键 |
| department_id | INTEGER | 所属部门ID |
| created_by | INTEGER | 创建人ID |
| status | INTEGER | 状态：1正常/0已销毁 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### files（文件表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| archive_id | INTEGER | 档案ID，外键 |
| file_name | TEXT | 文件名 |
| file_path | TEXT | 文件存储路径 |
| file_type | TEXT | 文件类型 |
| file_size | INTEGER | 文件大小（字节） |
| uploaded_by | INTEGER | 上传人ID |
| uploaded_at | DATETIME | 上传时间 |

#### permissions（权限表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| user_id | INTEGER | 用户ID，外键 |
| archive_id | INTEGER | 档案ID，外键 |
| permission_type | TEXT | 权限类型：manage/edit/add/view |

#### operation_logs（操作日志表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| user_id | INTEGER | 操作人ID |
| archive_id | INTEGER | 档案ID |
| operation_type | TEXT | 操作类型 |
| operation_time | DATETIME | 操作时间 |
| details | TEXT | 操作详情 |

#### access_applications（查档申请表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键，自增 |
| applicant_id | INTEGER | 申请人ID |
| archive_id | INTEGER | 档案ID |
| purpose | TEXT | 查询用途 |
| status | INTEGER | 状态：0待审核/1通过/2拒绝 |
| reviewer_id | INTEGER | 审核人ID |
| review_comment | TEXT | 审核意见 |
| created_at | DATETIME | 申请时间 |
| reviewed_at | DATETIME | 审核时间 |

## 4. 前端界面设计

### 4.1 设计风格
UI组件库采用Ant Design Vue，视觉风格参考Notion设计系统进行定制：
- Ant Design Vue基础组件（表格、表单、弹窗、菜单等）
- 定制主题：纯白背景（#ffffff）、温暖灰色文字
- Notion Blue主色调（#0075de）替换Ant Design默认蓝色
- 超细边框、柔和阴影
- Inter字体系列

### 4.2 页面结构

#### 登录页面
- 用户名密码输入
- Notion风格简洁界面
- 登录按钮

#### 主布局
- 左侧：功能菜单树（可折叠）
- 顶部：用户信息、退出按钮
- 右侧：主内容区

#### 功能页面
1. **档案管理**
   - 文件夹树形结构（a-tree组件）
   - 档案列表（a-table组件）
   - 文件上传组件（a-upload组件）
   - 档案信息编辑（a-form组件）

2. **档案查询**
   - 全文搜索框（a-input-search）
   - 高级搜索表单（a-form + a-collapse）
   - 搜索结果列表（a-table）
   - 在线预览入口

3. **档案阅读**
   - 文件预览区域
   - 支持OFFICE/PDF/图片格式
   - 下载按钮

4. **查档管理**
   - 申请列表（a-table）
   - 申请表单（a-modal + a-form）
   - 审核界面（部门管理员）

5. **系统管理**（仅管理员）
   - 用户管理（a-table + a-modal）
   - 部门管理（a-tree + a-modal）
   - 权限设置
   - 操作日志（a-table + a-range-picker筛选）

6. **仪表盘**（AntV图表）
   - 档案统计（G2柱状图/饼图）
   - 操作趋势（G2折线图）
   - 快捷操作入口

### 4.3 权限控制
- **系统管理员**：全部功能权限
- **部门管理员**：本部门档案管理、查档审核
- **普通用户**：档案查询、申请查阅

## 5. 核心功能流程

### 5.1 档案上传流程
1. 选择或创建文件夹
2. 填写档案信息（标题、类别、密级）
3. 上传文件（支持批量）
4. 系统自动记录操作日志

### 5.2 档案查询流程
1. 输入关键词或选择搜索条件
2. 全文检索或高级搜索
3. 展示搜索结果列表
4. 点击预览或申请查阅

### 5.3 查档申请流程
1. 用户选择目标档案
2. 填查查档用途
3. 提交申请
4. 部门管理员审核
5. 审核通过后可查阅/下载

### 5.4 权限管理流程
1. 系统管理员创建用户并分配角色
2. 部门管理员管理本部门用户权限
3. 四层文档权限控制
4. 六级查看权限控制

### 5.5 档案销毁流程
1. 用户选择失效档案
2. 提交销毁申请
3. 部门管理员审核
4. 执行删除操作

## 6. 安全设计

### 6.1 访问控制
- 基于角色的访问控制（RBAC）
- 四层文档权限：管理、编辑、添加、查看
- 六级查看权限：查看标题、预览内容、打印、下载、外发、查看保密文件

### 6.2 数据安全
- 密码加密存储
- 操作日志记录
- 文件访问权限控制
- 部门数据隔离

### 6.3 备份策略
- 人工备份数据库文件
- 人工备份程序文件
- 定期备份建议

## 7. 开发计划

### 7.1 第一阶段：前端界面开发
1. 项目初始化和基础配置（Vue CLI + Ant Design Vue）
2. 登录页面开发
3. 主布局框架开发（Ant Design ProLayout风格）
4. 各功能页面静态界面（使用Ant Design Vue组件）
5. 路由配置（Vue Router）
6. 状态管理基础设置（Vuex）
7. AntV图表集成（仪表盘数据可视化）

### 7.2 第二阶段：后端开发
1. SpringBoot项目搭建，配置多模块结构
2. SQLite数据库集成（MyBatis + SQLite驱动）
3. Spring Security用户认证（JWT Token）
4. Redis缓存集成（会话管理、数据缓存）
5. RabbitMQ消息队列集成（异步日志、通知）
6. 档案管理RESTful API开发
7. 文件上传下载API开发
8. 权限控制实现（RBAC）

### 7.3 第三阶段：功能集成
1. 前后端接口对接（Axios + 统一响应格式）
2. 文件预览功能实现
3. 查档审核流程（RabbitMQ通知）
4. 操作日志记录（RabbitMQ异步写入）
5. 系统测试和优化

## 8. 验收标准

### 8.1 功能验收
- 用户登录/退出功能正常
- 档案增删改查功能完整
- 文件上传下载正常
- 查档申请审核流程通畅
- 权限控制有效

### 8.2 界面验收
- 符合Notion设计风格
- 界面响应流畅
- 操作逻辑清晰

### 8.3 性能验收
- 页面加载时间<3秒
- 文件上传下载稳定
- 搜索响应及时
