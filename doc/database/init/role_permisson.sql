-- ================
-- === 初始化角色 ===
-- ================
-- 超级管理员角色
INSERT INTO role (name, description) VALUES ('super-idol', '超级管理员'); -- ^_^



-- ================
-- === 初始化权限 ===
-- ================

-- 角色权限
INSERT INTO permission (name, description) VALUES ('role:add', '添加角色');
INSERT INTO permission (name, description) VALUES ('role:update', '修改角色');
INSERT INTO permission (name, description) VALUES ('role:delete', '删除角色');
INSERT INTO permission (name, description) VALUES ('role:list', '查看角色列表');
INSERT INTO permission (name, description) VALUES ('role:add-permission', '给角色分配权限');
INSERT INTO permission (name, description) VALUES ('user:add-role', '给用户分配角色');

-- 权限权限
INSERT INTO permission (name, description) VALUES ('permission:list', '查看权限列表');

-- 题目权限
INSERT INTO permission (name, description) VALUES ('problem:add', '添加题目');
INSERT INTO permission (name, description) VALUES ('problem:update', '更新题目');
INSERT INTO permission (name, description) VALUES ('problem:add-tag', '为题目添加标签');
INSERT INTO permission (name, description) VALUES ('problem:delete-tag', '删除题目标签');
INSERT INTO permission (name, description) VALUES ('problem:add-test-case', '添加题目测试用例');
INSERT INTO permission (name, description) VALUES ('problem:delete-test-case', '删除题目测试用例');

-- 题目标签权限
INSERT INTO permission (name, description) VALUES ('problem-tag:add', '添加题目标签');

-- 比赛权限
INSERT INTO permission (name, description) VALUES ('contest:create', '创建比赛');
INSERT INTO permission (name, description) VALUES ('contest:list', '查看比赛列表');
INSERT INTO permission (name, description) VALUES ('contest:update', '更新比赛信息');
INSERT INTO permission (name, description) VALUES ('contest:delete', '删除比赛');
INSERT INTO permission (name, description) VALUES ('contest:add-problem-to-contest', '为比赛添加题目');
