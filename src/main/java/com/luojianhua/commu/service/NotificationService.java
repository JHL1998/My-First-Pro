package com.luojianhua.commu.service;

import com.luojianhua.commu.dto.NotificationDTO;
import com.luojianhua.commu.dto.PageDTO;
import com.luojianhua.commu.enums.NotificationStatusEnum;
import com.luojianhua.commu.enums.NotificationTypeEnum;
import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.exception.CustomizeException;
import com.luojianhua.commu.mapper.NotificationMapper;
import com.luojianhua.commu.model.Notification;
import com.luojianhua.commu.model.NotificationExample;
import com.luojianhua.commu.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;


    public PageDTO list(Long userId, Integer page, Integer size) {

        PageDTO<NotificationDTO> pageDTO=new PageDTO();

        Integer totalPage;

        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);

        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }

        if(page<1)
            page=1;
        if(page>totalPage )
            page=totalPage ;

        pageDTO.setPagination(totalPage,page);


        //size*(page-1)=count
        Integer offset=size*(page-1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if(notifications.size()==0){
            return pageDTO;
        }

        List<NotificationDTO>notificationDTOS=new ArrayList<>();
        for(Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
         pageDTO.setData(notificationDTOS);

        return pageDTO;
    }


    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
         return notificationMapper.countByExample(notificationExample) ;

    }

    public NotificationDTO read(Long id, User user) {

        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null)
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        if(notification.getReceiver()!=user.getId())
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        //标记已读
       notification.setStatus(NotificationStatusEnum.READ.getStatus());
       notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}
