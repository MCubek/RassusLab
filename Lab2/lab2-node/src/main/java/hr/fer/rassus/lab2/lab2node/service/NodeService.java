package hr.fer.rassus.lab2.lab2node.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.util.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 05/12/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NodeService {
    // Autowired
    private final ApplicationContext context;
    private final NodeUtil nodeUtil;
    private final SensorService sensorService;
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    @Value("${spring.kafka.register-topic}")
    private final String registerTopic;

    // Initialized
    private final Set<Node> peers = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper mapper = new ObjectMapper();


    public void commandMessageHandler(ConsumerRecord<String, JsonNode> stringJsonNodeConsumerRecord) {
        String message = stringJsonNodeConsumerRecord.value().get("message").asText();
        log.info("Command message received: {}", message);

        try {
            switch (message) {
                case "START" -> startNode();
                case "STOP" -> stopNode();
                default -> throw new IllegalArgumentException("Unknown message!");
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            stopNode();
        }

    }

    @SneakyThrows
    public void registerMessageHandler(ConsumerRecord<String, JsonNode> stringJsonNodeConsumerRecord) {
        JsonNode message = stringJsonNodeConsumerRecord.value();
        log.info("Registration received: {}", stringJsonNodeConsumerRecord.value().toString());

        Node peer = mapper.treeToValue(message, Node.class);
        if (! peer.equals(nodeUtil.thisNode()))
            peers.add(peer);
    }

    private void startNode() {
        JsonNode peer = null;
        try {
            peer = mapper.readTree(mapper.writeValueAsString(nodeUtil.thisNode()));
        } catch (JsonProcessingException e) {
            log.error("Error while mapping POJO to JSON.", e);
            throw new RuntimeException(e);
        }

        //Register node
        try {
            kafkaTemplate.send(registerTopic, peer).get(2L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error while registering node in kafka topic.", e);
            throw new RuntimeException(e);
        }

        //Start sensor
        try {
            sensorService.startSensor(peers);
        } catch (SocketException e) {
            log.error("Error while starting sensor.", e);
            throw new RuntimeException(e);
        }
    }

    private void stopNode() {
        sensorService.stopSensor();

        ((ConfigurableApplicationContext) context).close();
    }
}
