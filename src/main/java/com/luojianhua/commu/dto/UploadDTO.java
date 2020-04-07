package com.luojianhua.commu.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UploadDTO {

     //是否响应成功
    private Boolean success;
    //状态码
    private Integer code;
    //返回信息
    private String message;
    private Map<String,Object> data=new HashMap<>();
    private UploadDTO(){

    }

    public static UploadDTO ok(){
        UploadDTO uploadDTO=new UploadDTO();
        uploadDTO.setSuccess(true);
        uploadDTO.setCode(200);
        uploadDTO.setMessage("上传成功");
        return uploadDTO;
    }

    public static UploadDTO error(){
        UploadDTO uploadDTO=new UploadDTO();
        uploadDTO.setSuccess(false);
        uploadDTO.setCode(500);
        uploadDTO.setMessage("上传失败");
        return uploadDTO;
    }


    // 使用链式编程

    public UploadDTO success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public UploadDTO message(String message) {
        this.setMessage(message);
        return this;
    }

    public UploadDTO code(Integer code) {
        this.setCode(code);
        return this;
    }

    public UploadDTO data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }


    public UploadDTO data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }


}

