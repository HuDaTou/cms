package com.overthinker.cloud.web.entity.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author overH
 * <p>
 * 创建时间：2023/10/16 20:56
 * RabbitMq常量类
 */
public class RabbitConst {

    /**
     * 邮件队列
     */
    public static final String MAIL_QUEUE = "cms_email_queue";

    /**
     * 登录日志队列
     */
    public static final String LOG_LOGIN_QUEUE = "cms_log_login_queue";

    /**
     * 系统操作日志队列
     */
    public static final String LOG_SYSTEM_QUEUE = "cms_log_system_queue";
}
