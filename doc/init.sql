




CREATE TABLE `problem` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE COMMENT '主键id',
	`problem_id` VARCHAR(255) NOT NULL UNIQUE COMMENT '题目展示id',
	`title` VARCHAR(255) NOT NULL COMMENT '题目标题',
	`time_limit` INTEGER NOT NULL COMMENT '时间限制(ms)，默认为c/c++限制,其它语言为2倍',
	`memory_limit` INTEGER NOT NULL COMMENT '空间限制(mb)，默认为c/c++限制,其它语言为2倍',
	`stack_limit` INTEGER NOT NULL COMMENT '栈限制(mb)，默认为128',
	`description` TEXT(65535) NOT NULL COMMENT '内容描述',
	`input_description` TEXT(65535) NOT NULL COMMENT '输入描述',
	`output_description` TEXT(65535) NOT NULL,
	`examples` VARCHAR(255) NOT NULL COMMENT '输入输出样例',
	`source` VARCHAR(255) NOT NULL DEFAULT 'zzcoder' COMMENT '题目来源',
	`difficulty` INTEGER NOT NULL COMMENT '题目难度，0入门，1简单-，2简单+，3中等-，4中等+，5困难-，6困难+',
	`hint` VARCHAR(255) COMMENT '备注提醒',
	`status` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '默认0公开，1私有，3比赛中',
	`create_user` BIGINT NOT NULL COMMENT '创建人',
	`update_user` BIGINT NOT NULL COMMENT '修改人',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) COMMENT='题目表';

CREATE TABLE `tag` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`name` VARCHAR(32) UNIQUE COMMENT '标签名字',
`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) COMMENT='题目标签表';


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
