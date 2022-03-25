package com.edu.sekill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.edu.sekill.dao")
public class SeKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeKillApplication.class, args);
    }

}
