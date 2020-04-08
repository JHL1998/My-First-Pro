package com.luojianhua.commu.controller;

import com.luojianhua.commu.dto.CommentCreateDTO;
import com.luojianhua.commu.dto.CommentDTO;
import com.luojianhua.commu.dto.ResultDTO;
import com.luojianhua.commu.enums.CommentTypeEnum;
import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.model.Comment;
import com.luojianhua.commu.model.User;
import com.luojianhua.commu.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 一级评论
     * @param commentCreateDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/comment",method= RequestMethod.POST)
    public Object post( @RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user =(User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorof(CustomizeErrorCode.NO_LOGIN);
        }
        if(StringUtils.isBlank(commentCreateDTO.getContent()) ||commentCreateDTO==null){
            return ResultDTO.errorof(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
        return ResultDTO.correctof();



    }

    /**
     * 二级评论
     * @return
     */


    @ResponseBody

    @GetMapping("/comment/{id}")
    public ResultDTO<List> comments(@PathVariable("id") Long id){

        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.correctof(commentDTOS);
    }
}
