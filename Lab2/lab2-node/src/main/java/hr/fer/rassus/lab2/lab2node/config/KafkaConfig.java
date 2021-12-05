package hr.fer.rassus.lab2.lab2node.config;

import hr.fer.rassus.lab2.lab2node.service.NodeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.*;

/**
 * @author MatejCubek
 * @project RassusLab
 * @created 04/12/2021
 */
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    @Getter
    private String bootstrapAddress;

    @Value("consumer-${node-id}")
    @Getter
    private String consumerGroupId;


    @Value("${spring.kafka.register-topic}")
    @Getter
    private String registerTopic;

    @Value("${spring.kafka.command-topic}")
    @Getter
    private String commandTopic;

    @Bean
    public ProducerFactory<String, JsonNode> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, JsonNode> kafkaTemplate(ProducerFactory<String, JsonNode> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, JsonNode> consumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonNode.class);
        consumerProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "1800000");
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "1800000");

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    @SuppressWarnings("DuplicatedCode")
    @Bean
    public KafkaMessageListenerContainer<String, JsonNode> commandMessageListenerContainer(
            DefaultKafkaConsumerFactory<String, JsonNode> consumerFactory,
            NodeService nodeService) {

        List<String> consumerTopics = new ArrayList<>(List.of(commandTopic));

        ContainerProperties containerProps = new ContainerProperties(
                consumerTopics.toArray(new String[0]));
        containerProps.setGroupId(consumerGroupId);
        containerProps.setAckMode(ContainerProperties.AckMode.RECORD);
        containerProps.setMessageListener(
                (MessageListener<String, JsonNode>) nodeService::commandMessageHandler);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
    }

    @SuppressWarnings("DuplicatedCode")
    @Bean
    public KafkaMessageListenerContainer<String, JsonNode> registerCommandListenerContainer(
            DefaultKafkaConsumerFactory<String, JsonNode> consumerFactory,
            NodeService nodeService) {

        List<String> consumerTopics = new ArrayList<>(List.of(registerTopic));

        ContainerProperties containerProps = new ContainerProperties(
                consumerTopics.toArray(new String[0]));
        containerProps.setGroupId(consumerGroupId);
        containerProps.setAckMode(ContainerProperties.AckMode.RECORD);
        containerProps.setMessageListener(
                (MessageListener<String, JsonNode>) nodeService::registerMessageHandler);

        return new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
    }
}
