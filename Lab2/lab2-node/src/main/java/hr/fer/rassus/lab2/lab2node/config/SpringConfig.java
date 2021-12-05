package hr.fer.rassus.lab2.lab2node.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 05/12/2021
 */
@Component
public class SpringConfig {
    @Scheduled(fixedDelay = 1000 * 60 * 60) // every hour
    public void doNothing() {
        // Forces Spring Scheduling managing thread to start
    }
}
