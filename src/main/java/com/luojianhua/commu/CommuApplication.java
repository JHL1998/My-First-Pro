package com.luojianhua.commu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.luojianhua.commu.mapper")
public class CommuApplication {

	public static void main(String[] args) {

		SpringApplication.run(CommuApplication.class, args);
	}

}
