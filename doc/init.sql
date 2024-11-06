




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
	`auth` VARCHAR(255) NOT NULL DEFAULT '0' COMMENT '默认0公开，1私有，3比赛中',
	`create_user` BIGINT NOT NULL COMMENT '创建人',
	`update_user` BIGINT NOT NULL COMMENT '修改人',
	`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) COMMENT='题目表';