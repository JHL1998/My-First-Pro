package com.luojianhua.commu.advice;

import com.alibaba.fastjson.JSON;
import com.luojianhua.commu.dto.ResultDTO;
import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理异常信息
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request,
                  HttpServletResponse response){

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO=null;
            //返回json
            if(e instanceof CustomizeException){
               resultDTO= (ResultDTO) ResultDTO.errorof((CustomizeException) e);
            }else{
                resultDTO= ResultDTO.errorof(CustomizeErrorCode.SYS_ERROR);
            }
            try{
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer=response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();

            }catch(IOException ioe){

            }

        }else{
            //错误页面跳转
        }

        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else{
            model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
        }
        return new ModelAndView("error");
    }



}
