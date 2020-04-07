package com.luojianhua.commu.mapper;

import com.luojianhua.commu.dto.QuestionQueryDTO;
import com.luojianhua.commu.model.Question;

import java.util.List;

public interface QuestionMapperExt {
   int incView(Question record);
   int incCommentCount(Question record);
   List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}