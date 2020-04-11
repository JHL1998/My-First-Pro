package com.luojianhua.commu.service;

import com.luojianhua.commu.dto.PageDTO;
import com.luojianhua.commu.dto.QuestionDTO;
import com.luojianhua.commu.dto.QuestionQueryDTO;
import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.exception.CustomizeException;
import com.luojianhua.commu.mapper.QuestionMapper;
import com.luojianhua.commu.mapper.QuestionMapperExt;
import com.luojianhua.commu.mapper.UserMapper;
import com.luojianhua.commu.model.Question;
import com.luojianhua.commu.model.QuestionExample;
import com.luojianhua.commu.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Service层即中间层
 */
@Service
public class QuestionService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionMapperExt questionMapperExt;

    public PageDTO find(String tag, String search, Integer page, Integer size){


        if (StringUtils.isNotBlank(search)) {
            String []tags=StringUtils.split(search," ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        PageDTO pageDTO=new PageDTO();
       Integer totalPage;


        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);
        Integer totalCount=questionMapperExt.countBySearch(questionQueryDTO);


        if(totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }

        if(page<1)
            page=1;
        if(page>totalPage)
            page=totalPage ;
        pageDTO.setPagination(totalPage,page);
        //size*(page-1)=count

        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question>questions=questionMapperExt.selectBySearch(questionQueryDTO);
        List<QuestionDTO>questionDTOList=new ArrayList<>();

        for(Question question:questions){

           User user= userMapper.selectByPrimaryKey(question.getCreator());
           QuestionDTO questionDTO=new QuestionDTO();
            // BeanUtils方法代替了 往questionDTO 中重复set的步骤
            //即快速的将question中的所有属性，拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        pageDTO.setData(questionDTOList);
        return pageDTO;


    }

    public PageDTO find(Long userId, Integer page, Integer size) {
        PageDTO pageDTO=new PageDTO();

        Integer totalPage;


        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

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
        QuestionExample example=new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question>questions=questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        List<QuestionDTO>questionDTOList=new ArrayList<>();

        for(Question question:questions){

            User user= userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            // BeanUtils方法代替了 往questionDTO 中重复set的步骤
            //即快速的将question中的所有属性，拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);

        }
        pageDTO.setData(questionDTOList);


        return pageDTO;

    }

    public QuestionDTO getById(Long id) {
       Question question= questionMapper.selectByPrimaryKey(id);
       if(question==null){
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
       }
       QuestionDTO questionDTO=new QuestionDTO();
       BeanUtils.copyProperties(question,questionDTO);
       //传入User 这样question.user.name才会有值
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);


       return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建新的问题

            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            //更新该问题
            Question updateQuestion=new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample=new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());

            int updated = questionMapper.updateByExampleSelective(updateQuestion,questionExample);
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);

            }


        }
    }

    public void incView(Long id) {


        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionMapperExt.incView(question);


    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionMapperExt.selectRelated(question);


        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
