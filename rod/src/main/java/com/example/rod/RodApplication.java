package com.example.rod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
// For Test. 시큐리티 설정 해제.
//@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@SpringBootApplication
public class RodApplication {

    public static void main(String[] args) {
        SpringApplication.run(RodApplication.class, args);
    }

}
