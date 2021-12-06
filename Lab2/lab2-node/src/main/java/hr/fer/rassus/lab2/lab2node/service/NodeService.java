package hr.fer.rassus.lab2.lab2node.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.rassus.lab2.lab2node.model.Node;
import hr.fer.rassus.lab2.lab2node.util.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    // Initialized
    private final Set<Node> peers = Collections.synchronizedSet(new HashSet<>());
    private final ObjectMapper mapper = new ObjectMapper();


    public void commandMessageHandler(ConsumerRecord<String, JsonNode> stringJsonNodeConsumerRecord) {
        String message = stringJsonNodeConsumerRecord.value().get("message").asText();
        log.info("Command message received: {}", message);

        switch (message) {
            case "START" -> startNode();
            case "STOP" -> stopNode();
            default -> throw new IllegalArgumentException("Unknown message!");
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
        sensorService.startSensor(peers);
    }

    private void stopNode() {
        sensorService.stopSensor();

        ((ConfigurableApplicationContext) context).close();
    }
}
