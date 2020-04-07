package com.luojianhua.commu.controller;

import com.luojianhua.commu.dto.FileDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @PostMapping("/file/upload")
    public FileDTO upload(){

        FileDTO fileDTO=new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/img/head.png");
        return fileDTO;
    }

}

