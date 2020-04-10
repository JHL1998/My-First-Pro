package com.luojianhua.commu.dto;

import lombok.Data;

@Data
public class HotTagDTO implements Comparable {
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        //最小堆，希望取出最小元素，即队列里面都是较大的元素
        return this.getPriority()-((HotTagDTO)o).getPriority();
    }
}
