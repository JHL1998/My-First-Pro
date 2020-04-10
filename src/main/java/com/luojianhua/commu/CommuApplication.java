package com.luojianhua.commu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.luojianhua.commu.mapper")
@EnableScheduling
public class CommuApplication {

	public static void main(String[] args) {

		SpringApplication.run(CommuApplication.class, args);
	}

}
