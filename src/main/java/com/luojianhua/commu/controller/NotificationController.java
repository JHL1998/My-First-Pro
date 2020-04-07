package com.luojianhua.commu.controller;

import com.luojianhua.commu.dto.NotificationDTO;
import com.luojianhua.commu.enums.NotificationTypeEnum;
import com.luojianhua.commu.model.User;
import com.luojianhua.commu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Long id,
                                         HttpServletRequest request){

       //验证是否登陆
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
       NotificationDTO notificationdto= notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType()==notificationdto.getType()
        || NotificationTypeEnum.REPLY_QUESTION.getType()==notificationdto.getType()){
            return "redirect:/question/"+notificationdto.getOuterId();
        }else{
            return "profile";
        }
    }

}
