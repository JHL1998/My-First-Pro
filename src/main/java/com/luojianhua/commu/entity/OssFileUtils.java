package com.luojianhua.commu.entity;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class OssFileUtils {


        private static String endpoint = "oss-on-zhangjiakou.aliyuncs.cn";

        private static String accessKeyId = "LTAI4FdQRqsPvqSruQmh3BCs";

        private static String accessKeySecret = "JZW99gjxGjua6KMsTs88UAGJgTapue";

        private static String bucketName = "community-fileupload";

        /**
         * 功能描述:
         *
         * @param:[objectKey, multipartFile 文件的新名称]
         * @return:java.lang.String
         *
         **/
        public static String uploadFile(String objectKey, MultipartFile multipartFile)
                throws OSSException, ClientException, FileNotFoundException {

            // 创建OSSClient的实例
            OSSClient ossClient  = new OSSClient(endpoint,accessKeyId,accessKeySecret);

            StringBuffer sb = new StringBuffer();
            // 上传的文件不是空，并且文件的名字不是空字符串
            if (multipartFile.getSize() != 0 && !"".equals(multipartFile.getName())) {
                ObjectMetadata om = new ObjectMetadata();
                om.setContentLength(multipartFile.getSize());
                // 设置文件上传到服务器的名称
                om.addUserMetadata("filename", objectKey);
                try {
                    ossClient.putObject(bucketName, objectKey, new ByteArrayInputStream(multipartFile.getBytes()), om);
                } catch (IOException e) {

                }
            }
            // 设置这个文件地址的有效时间
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
            String url = ossClient.generatePresignedUrl(bucketName, objectKey, expiration).toString();
            return url;
        }
    }



