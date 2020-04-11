package com.luojianhua.commu.cache;

import com.luojianhua.commu.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {

    //直接存热门标签的名字
    private List<String> hot=new ArrayList<>();

    // 堆排序
    public void updateTags(Map<String,Integer> tags){
         int max=10;
        PriorityQueue<HotTagDTO> priorityQueue=new PriorityQueue<>(max);
        tags.forEach((name,priority)->{
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);

            if(priorityQueue.size()<10){
                priorityQueue.add(hotTagDTO);
            }else{
                //拿到最小的元素
                HotTagDTO minHot = priorityQueue.peek();
                //需要放进队列
                if(hotTagDTO.compareTo(minHot)>0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }

            }
        });

        List<String> sorted =new ArrayList<>();
        HotTagDTO poll=priorityQueue.poll();
        while(poll!=null){
            sorted.add(0,poll.getName());
            poll=priorityQueue.poll();

        }
        hot=sorted;


    }
}
