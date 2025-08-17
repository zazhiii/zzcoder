-- ===============
-- === 用户模块 ===
-- ===============

-- 用户表
drop table if exists user;
create table user
(
    id            bigint auto_increment primary key,
    username      varchar(32)         not null unique comment '用户名',
    password      varchar(128)        not null comment '登录密码',
    email         varchar(32)         not null unique comment '邮箱',
    phone_number  varchar(11)         null unique comment '手机号码',
    avatar_url    varchar(128)        null unique comment '头像图片地址',
    cf_username   varchar(32)         null unique comment 'Codeforces用户名',
    status        int       default 0 null comment '账号状态, 0可用, 1不可用',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) comment '用户表';

-- 角色表
drop table if exists `role`;
CREATE TABLE `role`
(
    `id`          INT         NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255) COMMENT '角色描述',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_name` (`name`)
) COMMENT ='角色表';

-- 权限表
drop table if exists `permission`;
CREATE TABLE `permission`
(
    `id`          INT         NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '权限名称',
    `description` VARCHAR(255) COMMENT '权限描述',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_name` (`name`)
) COMMENT ='权限表';

-- 用户-角色关联表
drop table if exists `user_role`;
CREATE TABLE `user_role`
(
    `user_id`     INT NOT NULL COMMENT '用户ID',
    `role_id`     INT NOT NULL COMMENT '角色ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
    PRIMARY KEY (`user_id`, `role_id`)
) COMMENT ='用户-角色关联表';

-- 角色-权限关联表
drop table if exists `role_permission`;
CREATE TABLE `role_permission`
(
    `role_id`       INT NOT NULL COMMENT '角色ID',
    `permission_id` INT NOT NULL COMMENT '权限ID',
    `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
    PRIMARY KEY (`role_id`, `permission_id`)
) COMMENT ='角色-权限关联表';

-- ===============
-- === 题目模块 ===
-- ===============

-- 题目表
drop table if exists problem;
create table problem
(
    id                 int auto_increment UNIQUE comment '主键id',
    problem_id         varchar(255) UNIQUE            not null comment '题目展示id, 如：P0001',
    title              varchar(255)                   not null comment '题目标题',
    time_limit         int                            not null comment '时间限制(ms)，默认为c/c++限制,其它语言为2倍',
    memory_limit       int                            not null comment '空间限制(mb)，默认为c/c++限制,其它语言为2倍',
    stack_limit        int                            not null comment '栈限制(mb)，默认为128',
    description        mediumtext                     not null comment '内容描述',
    input_description  mediumtext                     not null comment '输入描述',
    output_description mediumtext                     not null comment '输出描述',
    source             varchar(255) default 'zzcoder' not null comment '题目来源',
    difficulty         int                            not null comment '题目难度，0入门，1简单，2简单+，3中等，4中等+，5困难，6困难+',
    hint               varchar(255)                   null comment '备注提醒',
    status             varchar(255) default '0'       not null comment '默认0公开，1私有，3比赛中',
    create_user        bigint                         not null comment '创建人',
    update_user        bigint                         not null comment '修改人',
    create_time        timestamp    default CURRENT_TIMESTAMP comment '创建时间',
    update_time        timestamp    default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`)
) comment '题目表';

-- 题目标签表
drop table if exists `tag`;
CREATE TABLE `tag`
(
    `id`          INT NOT NULL PRIMARY KEY AUTO_INCREMENT UNIQUE,
    `name`        VARCHAR(32) UNIQUE COMMENT '标签名字',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='题目标签表';

-- 题目标签关联表
drop table if exists `problem_tag`;
CREATE TABLE `problem_tag`
(
    `id`          INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `pid`         INTEGER NOT NULL COMMENT '题目id',
    `tid`         INTEGER NOT NULL COMMENT '标签id',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='题目标签表';

-- ===============
-- === 题目模块 ===
-- ===============

-- 题单表
drop table if exists problem_set;
create table problem_set
(
    id          int auto_increment primary key comment '主键，自增长',
    title       varchar(255)                        not null comment '题单标题',
    description text                                null comment '题单描述',
    status      tinyint   default 0                 not null comment '状态（0私有，1公开）',
    create_user bigint                              null comment '创建人（用户ID）',
    update_user bigint                              null comment '修改人（用户ID）',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '题单表';

-- 题目-题单关联表
drop table if exists problem_problem_set;
create table problem_problem_set
(
    id             int auto_increment primary key comment '主键，自增长',
    problem_set_id int                                 not null comment '题单ID',
    problem_id     int                                 not null comment '题目ID',
    create_time    timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '题目-题单关联表';

-- ===============
-- === 判题模块 ===
-- ===============

-- 提交记录表
DROP TABLE IF EXISTS submission;
CREATE TABLE submission
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    submit_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    problem_id    INT    NOT NULL COMMENT '题目id',
    user_id       BIGINT NOT NULL COMMENT '用户id',
    contest_id    BIGINT NOT NULL COMMENT '比赛id, 非比赛提交则为0',
    language      VARCHAR(50) COMMENT '编程语言',
    code          TEXT COMMENT '代码',
    status        VARCHAR(50) COMMENT '当前判题状态：PENDING, JUDGING, AC, ...',
    error_message TEXT COMMENT '错误信息',
    time_used     INT COMMENT '运行时间（ms）',
    memory_used   INT COMMENT '内存使用（KB）',
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 测试用例表
DROP TABLE IF EXISTS test_case;
CREATE TABLE test_case
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    problem_id  INT  NOT NULL COMMENT '关联题目的ID',
    input       TEXT NOT NULL COMMENT '输入数据',
    output      TEXT NOT NULL COMMENT '期望输出',
    is_sample   BOOLEAN   DEFAULT FALSE COMMENT '是否是示例用例（用于展示）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- ===============
-- === 竞赛模块 ===
-- ===============

-- 竞赛表
DROP TABLE IF EXISTS contest;
CREATE TABLE contest
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '竞赛ID',
    title       VARCHAR(255) NOT NULL COMMENT '竞赛标题',
    description TEXT COMMENT '竞赛描述',
    start_time  TIMESTAMP    NOT NULL COMMENT '开始时间',
    end_time    TIMESTAMP    NOT NULL COMMENT '结束时间',
    duration    INT          NOT NULL COMMENT '持续时间（分钟）',
    status      TINYINT   DEFAULT 0 COMMENT '竞赛状态（0未开始，1进行中，2已结束）',
    visible     TINYINT   DEFAULT 0 COMMENT '竞赛权限（0公开，1私有，2密码保护）',
    type        TINYINT   DEFAULT 0 COMMENT '竞赛类型（acm, ioi, oi...）',
    password    VARCHAR(255) COMMENT '竞赛密码（仅当 type=2 时使用）',
    create_user BIGINT       NOT NULL COMMENT '创建人（用户ID）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '竞赛表';

-- 竞赛-题目关联表
DROP TABLE IF EXISTS contest_problem;
CREATE TABLE contest_problem
(
    id         INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    display_id VARCHAR(255) NOT NULL COMMENT '题目展示ID: A B C ...',
    contest_id BIGINT       NOT NULL COMMENT '竞赛ID',
    problem_id BIGINT       NOT NULL COMMENT '题目ID'
) COMMENT '竞赛题目关联表';

-- 竞赛-参加用户关联表
DROP TABLE IF EXISTS contest_user;
CREATE TABLE contest_user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    contest_id   BIGINT NOT NULL COMMENT '竞赛ID',
    user_id      BIGINT NOT NULL COMMENT '用户ID',
    score        INT DEFAULT 0 COMMENT '当前得分',
    submit_count INT DEFAULT 0 COMMENT '提交次数'
) COMMENT '竞赛参加用户关联表';

-- 竞赛提交表
DROP TABLE IF EXISTS contest_submission;
CREATE TABLE contest_submission
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    contest_id  BIGINT      NOT NULL COMMENT '竞赛ID',
    user_id     BIGINT      NOT NULL COMMENT '用户ID',
    problem_id  BIGINT      NOT NULL COMMENT '题目ID',
    code        TEXT        NOT NULL COMMENT '提交的代码',
    language    VARCHAR(50) NOT NULL COMMENT '编程语言',
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开赛后提交时间',
    status      TINYINT   DEFAULT 0 COMMENT '评测状态（0待评测，1通过，2错误等）',
    score       INT       DEFAULT 0 COMMENT '提交得分'
) COMMENT '竞赛提交表';