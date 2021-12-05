package hr.fer.rassus.lab2.lab2node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Lab2NodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab2NodeApplication.class, args);
    }

}
