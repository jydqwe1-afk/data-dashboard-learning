package com.archive.common;

public class Constants {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_DEPT_ADMIN = "dept_admin";
    public static final String ROLE_USER = "user";

    public static final String PERM_MANAGE = "manage";
    public static final String PERM_EDIT = "edit";
    public static final String PERM_ADD = "add";
    public static final String PERM_VIEW = "view";

    public static final String MQ_LOG_EXCHANGE = "archive.log.exchange";
    public static final String MQ_LOG_QUEUE = "archive.log.queue";
    public static final String MQ_LOG_ROUTING_KEY = "archive.log";

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DELETED = 0;
}
