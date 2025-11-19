package org.cn.personalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PersonalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalApiApplication.class, args);
    }

}
