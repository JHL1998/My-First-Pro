package com.luojianhua.commu.cache;

import com.luojianhua.commu.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    public  static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        //编程语言
        TagDTO program = new TagDTO();

        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("Java", "Python", "C++", "C", "JavaScrpit", "html", "php", "node", "golang", "swift"));
        tagDTOS.add(program);

        //平台架构
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台架构");
        framework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "koa", "struts"));
        tagDTOS.add(framework);

        //服务器
        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux", "nginx", "docker", "apache", "ubuntu", "centos", "tomcat", "unix", "hadoop", "window-server"));
        tagDTOS.add(server);

        //数据库
        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql", "sqlserver", "sqlite", "postgresql"));
        tagDTOS.add(db);

        //开发工具
        TagDTO tool=new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","visual-studio"));
        tagDTOS.add(tool);

        return tagDTOS;

    }

    public  static String  filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO>tagDTOS=get();
        //将二维数组两层的东西循环出来

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return invalid;
    }
}
