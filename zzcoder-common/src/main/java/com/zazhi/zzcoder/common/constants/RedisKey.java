package com.zazhi.zzcoder.common.constants;

/**
 *
 * @author lixh
 * @since 2025/9/16 11:04
 */
public class RedisKey {
    public static final String LOGIN = "login:%s";

    public static final String ROLE = "role:%s";

    public static final String PERMISSION = "permission:%s";

    public static final String AUTHORITIES = "authorities:%s";

    public static String format(String format, Object... args) {
        return String.format(format, args);
    }
}
