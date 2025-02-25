//package com.overthinker.cloud.web.entity.myConfigProperties;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//@Component
//@ConfigurationProperties(prefix = "spring.rabbitmq.queue")
//public class RabbitConfigProperties {
//
//    private static String EMAIL_QUEUE;
//    private static String LOG_SYSTEM_QUEUE;
//    private static String LOG_LOGIN_QUEUE;
//
//    // 非静态的 setter 方法（名称要与配置键匹配）
//    public void setEmailQueue(String emailQueue) {
//        EMAIL_QUEUE = emailQueue;
//    }
//
//    public void setLogSystemQueue(String logSystemQueue) {
//        LOG_SYSTEM_QUEUE = logSystemQueue;
//    }
//
//    public void setLogLoginQueue(String logLoginQueue) {
//        LOG_LOGIN_QUEUE = logLoginQueue;
//    }
//
//    // 提供静态访问方法
//    public static String getEmailQueue() {
//        return EMAIL_QUEUE;
//    }
//
//    public static String getLogSystemQueue() {
//        return LOG_SYSTEM_QUEUE;
//    }
//
//    public static String getLogLoginQueue() {
//        return LOG_LOGIN_QUEUE;
//    }
//}