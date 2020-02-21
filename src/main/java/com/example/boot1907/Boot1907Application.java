package com.example.boot1907;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.boot1907.dao")
public class Boot1907Application extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(Boot1907Application.class, args);
    }

}
