package hr.fer.rassus.lab3.humiditymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HumidityMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumidityMicroserviceApplication.class, args);
    }

}
