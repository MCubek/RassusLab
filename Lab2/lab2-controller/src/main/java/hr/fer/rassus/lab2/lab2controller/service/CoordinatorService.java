package hr.fer.rassus.lab2.lab2controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 04/12/2021
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinatorService {
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${spring.kafka.topic}")
    @Getter
    private String topic;

    public ResponseEntity<Void> start() {
        try {
            kafkaTemplate.send(topic, startMessage).get(2L, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("Error while sending start message to kafka topic.", e);
            return ResponseEntity.internalServerError().build();
        }
        log.info("START request sent to kafka.");
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> stop() {
        try {
            kafkaTemplate.send(topic, stopMessage).get(2L, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("Error while sending stop message to kafka topic.", e);
            return ResponseEntity.internalServerError().build();
        }
        log.info("STOP request sent to kafka.");
        return ResponseEntity.ok().build();
    }

    private final JsonNode startMessage = mapper.createObjectNode().put("message", "START");
    private final JsonNode stopMessage = mapper.createObjectNode().put("message", "STOP");
}
