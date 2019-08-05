package com.dy.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 
 * @author caiwl
 * 2018-06-08 08:56:08
 */
@SpringBootApplication
@MapperScan("com.aloestec.framework.mapper")
public class FrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkApplication.class, args);
    }

}
