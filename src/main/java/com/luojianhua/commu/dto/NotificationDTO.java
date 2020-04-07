package com.luojianhua.commu.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long  notifier;
    private String notificationName;
    private String outerTitle;
    private String typeName;
    private Long outerId;
    private Integer type;
}
