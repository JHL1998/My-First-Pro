package com.luojianhua.commu.dto;

import com.luojianhua.commu.exception.CustomizeErrorCode;
import com.luojianhua.commu.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<E>{

    private String message;
    private Integer code;
    private E data;

    public static ResultDTO errorof(Integer code,String message){

        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;

    }

    public static ResultDTO errorof(CustomizeErrorCode errorCode) {
        return errorof(errorCode.getCode(),errorCode.getMessage());
    }

    public static Object errorof(CustomizeException e) {
        return errorof(e.getCode(),e.getMessage());
    }

    public static ResultDTO correctof(){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <E>ResultDTO correctof(E e){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(e);
        return resultDTO;

    }

}
