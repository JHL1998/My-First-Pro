package com.luojianhua.commu.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你找的问题不存在，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何评论或者问题进行回复"),
    NO_LOGIN(2003,"当前操作需要登录状态，请先登录"),
    SYS_ERROR(2004,"服务冒烟了，要不然稍后你再试试!!!"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你找的评论不存在，要不要换一个试试？" ),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008,"不能读别人的信息"),
    NOTIFICATION_NOT_FOUND(2009,"通知不存在");
    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
