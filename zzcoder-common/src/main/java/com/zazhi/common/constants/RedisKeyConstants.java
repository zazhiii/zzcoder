package com.zazhi.common.constants;

public class RedisKeyConstants {

    // ========== 登录相关 ==========
    public static final String LOGIN_TOKEN = "login:token:%s"; // 用户登录Token
    public static final String LOGIN_CAPTCHA = "login:captcha:%s";
    public static final String JWT_TOKEN = "jwt:token:%s";

    // ========== 注册相关 ==========
    public static final String REGISTER_EMAIL_CODE = "register:email-code:%s"; // 注册邮箱验证码

    // ========== 验证码相关 ==========
    public static final String EMAIL_CODE = "%s:email-code:%s"; // 邮箱验证码 <业务>:email-code:<邮箱>

    // ========== 用户相关 ==========
    public static final String USER_INFO = "user:info:%s"; // 用户基础信息缓存
    public static final String USER_ROLE_PERMISSIONS = "user:role-permissions:%s"; // 用户角色和权限缓存

    // ========== 权限管理 ==========
    public static final String USER_PERMISSIONS = "user:permissions:%s"; // 用户权限集合缓存
    public static final String ROLE_PERMISSIONS = "role:permissions:%s"; // 角色对应权限缓存

    // ========== 比赛相关 ==========
    public static final String CONTEST_DETAIL = "contest:detail:%s"; // 比赛详情缓存
    public static final String CONTEST_PROBLEMS = "contest:problems:%s"; // 比赛题目列表缓存

    // ========== 提交判题相关 ==========
    public static final String JUDGE_STATUS = "judge:status:%s"; // 判题状态缓存
    public static final String JUDGE_RESULT = "judge:result:%s"; // 判题结果缓存

    // ========== 访问频率限制 ==========
    public static final String RATE_LIMIT_IP = "limit:ip:%s"; // IP限流
    public static final String RATE_LIMIT_USER = "limit:user:%s"; // 用户限流

    // ========== 方法 ==========
    public static String format(String keyPattern, Object... args) {
        return String.format(keyPattern, args);
    }
}
