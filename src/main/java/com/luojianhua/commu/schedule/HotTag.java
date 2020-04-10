package com.luojianhua.commu.schedule;

import com.luojianhua.commu.cache.HotTagCache;
import com.luojianhua.commu.mapper.QuestionMapper;
import com.luojianhua.commu.model.Question;
import com.luojianhua.commu.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTag {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 1 * * *")
    public void hotTag() {
        int offset = 0;
        int limit = 5;
        Map<String, Integer> tagMap = new HashMap<>();
        log.info("The time is now{}", new Date());
        List<Question> list = new ArrayList<>();
        //如果不是第一页，或者还有下一页
        while (offset == 0 || list.size() == limit) {
            list = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : list) {

                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    Integer priority = tagMap.get(tag);
                    if (priority != null) {
                        tagMap.put(tag, priority + 5 + question.getCommentCount());
                    } else {
                        tagMap.put(tag, 5 + question.getCommentCount());
                    }
                }

                offset += limit;
            }
            hotTagCache.setTags(tagMap);
            hotTagCache.getTags().forEach((k,v)->{
                System.out.print(k);
                System.out.print(":");
                System.out.print(v);
                System.out.println();

            });

        }
    }
}
