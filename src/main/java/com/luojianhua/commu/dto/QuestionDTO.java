package com.luojianhua.commu.dto;

import com.luojianhua.commu.model.User;
import lombok.Data;

/**
 * 通过ID获取到User表中的头像信息
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creatorId;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;

}
