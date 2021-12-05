package hr.fer.rassus.lab2.lab2node.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 05/12/2021
 */
@Service
@Slf4j
public class NodeService {

    public void commandMessageHandler(ConsumerRecord<String, JsonNode> stringJsonNodeConsumerRecord) {
        log.info(stringJsonNodeConsumerRecord.value().toPrettyString());
    }

    public void registerMessageHandler(ConsumerRecord<String, JsonNode> stringJsonNodeConsumerRecord) {
        log.info(stringJsonNodeConsumerRecord.value().toPrettyString());
    }
}
