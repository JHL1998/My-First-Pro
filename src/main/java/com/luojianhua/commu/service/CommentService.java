package com.luojianhua.commu.service;

import com.luojianhua.commu.dto.CommentDTO;
import com.luojianhua.commu.enums.CommentTypeEnum;
import com.luojianhua.commu.enums.NotificationStatusEnum;
import com.luojianhua.commu.enums.NotificationTypeEnum;
import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.exception.CustomizeException;
import com.luojianhua.commu.mapper.*;
import com.luojianhua.commu.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionMapperExt questionMapperExt;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapperExt commentMapperExt;
    @Autowired
    private NotificationMapper notificationMapper;

    //添加事务
    @Transactional
    public void insert(Comment comment, User commentator) {

        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExists(comment.getType())) {

            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }


            Question question=questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //增加评论数
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());

            parentComment.setCommentCount(1);
            commentMapperExt.incCommentCount(parentComment);
            //创建通知
           createNotify(comment, dbComment.getCommentatorId(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT,question.getId());
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());

            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
               comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionMapperExt.incCommentCount(question);

             //创建通知
            createNotify(comment,question.getCreatorId(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());

        }
    }


    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType,Long outerId) {
         //接收通知的人和触发通知的人，是同一人
        if(receiver==comment.getCommentatorId()){

               return;
           }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        //评论人
        notification.setNotifier(comment.getCommentatorId());
        //状态
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        //接收到通知的人
        notification.setReceiver(receiver);
        notification.setNotificationName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);

    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().
                andParentIdEqualTo(id).
                andTypeEqualTo(type.getType());
        //按时间倒序排列
        commentExample.setOrderByClause("gmt_create desc");

        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) return new ArrayList<>();
        //获取去重的评论人
        Set<Long> commentators = comments.stream().
                map(comment -> comment.getCommentatorId()).
                collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为Map，增加查询效率
        UserExample UserExample = new UserExample();
        UserExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(UserExample);
        Map<Long, User> userMap = users.stream().
                collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment 为commentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentatorId()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOList;
    }

}
