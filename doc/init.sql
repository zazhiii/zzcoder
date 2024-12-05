-- 题目-题单关联表
drop table if exists problem_problem_set;
create table problem_problem_set
(
    id          int auto_increment primary key comment '主键，自增长',
    problem_set_id int not null comment '题单ID',
    problem_id     int not null comment '题目ID',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '题目-题单关联表';

-- 题单表
drop table if exists problem_set;
create table problem_set
(
    id          int auto_increment primary key comment '主键，自增长',
    title       varchar(255)                        not null comment '题单标题',
    description text                                null comment '题单描述',
    status      tinyint default 0                   not null comment '状态（0私有，1公开）',
    create_user bigint                              null comment '创建人（用户ID）',
    update_user bigint                              null comment '修改人（用户ID）',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '题单表';

-- 题目表
drop table if exists problem;
create table problem
(
    id                 int auto_increment UNIQUE                       comment '主键id',
    problem_id         varchar(255) UNIQUE                    not null comment '题目展示id',
    title              varchar(255)                           not null comment '题目标题',
    time_limit         int                                    not null comment '时间限制(ms)，默认为c/c++限制,其它语言为2倍',
    memory_limit       int                                    not null comment '空间限制(mb)，默认为c/c++限制,其它语言为2倍',
    stack_limit        int                                    not null comment '栈限制(mb)，默认为128',
    description        mediumtext                             not null comment '内容描述',
    input_description  mediumtext                             not null comment '输入描述',
    output_description mediumtext                             not null,
    source             varchar(255) default 'zzcoder'         not null comment '题目来源',
    difficulty         int                                    not null comment '题目难度，0入门，1简单-，2简单+，3中等-，4中等+，5困难-，6困难+',
    hint               varchar(255)                           null comment '备注提醒',
    status             varchar(255) default '0'               not null comment '默认0公开，1私有，3比赛中',
    create_user        bigint                                 not null comment '创建人',
    update_user        bigint                                 not null comment '修改人',
    create_time        timestamp    default CURRENT_TIMESTAMP comment '创建时间',
    update_time        timestamp    default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`)
) comment '题目表';

-- 题目标签表
CREATE TABLE `tag` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`name` VARCHAR(32) UNIQUE COMMENT '标签名字',
`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) COMMENT='题目标签表';

-- 题目标签关联表
CREATE TABLE `problem_tag` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`pid` INTEGER NOT NULL COMMENT '题目id',
	`tid` INTEGER NOT NULL COMMENT '标签id',
`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) COMMENT='题目标签表';

-- 角色表
CREATE TABLE `role` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(255) COMMENT '角色描述',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_name` (`name`)
) COMMENT='角色表';

-- 权限表
CREATE TABLE `permission` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `description` VARCHAR(255) COMMENT '权限描述',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_name` (`name`)
) COMMENT='权限表';

-- 用户-角色关联表
CREATE TABLE `user_role` (
    `user_id` INT NOT NULL COMMENT '用户ID',
    `role_id` INT NOT NULL COMMENT '角色ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
    PRIMARY KEY (`user_id`, `role_id`)
) COMMENT='用户-角色关联表';

-- 角色-权限关联表
CREATE TABLE `role_permission` (
    `role_id` INT NOT NULL COMMENT '角色ID',
    `permission_id` INT NOT NULL COMMENT '权限ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关联创建时间',
    PRIMARY KEY (`role_id`, `permission_id`)
) COMMENT='角色-权限关联表';

-- 提交记录表
CREATE TABLE submission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    submit_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    problem_id INT NOT NULL COMMENT '题目id',
    user_id BIGINT NOT NULL COMMENT '用户id',
    contest_id BIGINT NOT NULL COMMENT '比赛id, 非比赛提交则为0',
    language VARCHAR(50) COMMENT '编程语言',
    code TEXT COMMENT '代码',
    status VARCHAR(50) COMMENT '当前判题状态：Pending, Judging, Completed',
    result TEXT COMMENT '判题结果',
    error_message TEXT COMMENT '错误信息',
    time_used INT COMMENT '运行时间（ms）',
    memory_used INT COMMENT '内存使用（KB）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 测试用例表
CREATE TABLE test_case (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    problem_id INT NOT NULL COMMENT '关联题目的ID',
    input TEXT NOT NULL COMMENT '输入数据',
    expected_output TEXT NOT NULL COMMENT '期望输出',
    is_sample BOOLEAN DEFAULT FALSE COMMENT '是否是示例用例（用于展示）',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
);
