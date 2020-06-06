package com.example.boot1907;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.boot1907.dao")
//@EnableCaching
public class Boot1907Application extends SpringBootServletInitializer{

//    @Bean
//    public FilterRegistrationBean csrfFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new CsrfFilter(new HttpSessionCsrfTokenRepository()));
//        registration.addUrlPatterns("/*");
//        return registration;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Boot1907Application.class, args);
    }

}
