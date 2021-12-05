package hr.fer.rassus.lab2.lab2node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class Lab2NodeApplication {

    public static void main(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(List.of(args));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Input id:");
        argsList.add("--node-id=" + scanner.nextLine());
        System.out.println("Input port:");
        argsList.add("--node-port=" + scanner.nextLine());

        scanner.close();

        SpringApplication.run(Lab2NodeApplication.class, argsList.toArray(new String[0]));
    }

}
