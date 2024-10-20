package com.zuofw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.zuofw.mapper")
@EnableAsync
@EnableFeignClients("com.zuofw")
public class ZuofwNoteApplication {
    public static void main( String[] args ) {
        SpringApplication.run(ZuofwNoteApplication.class, args);
    }
}
