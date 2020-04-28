package com.cedrus.pingpong.kafka;

import com.cedrus.pingpong.config.KafkaConfig;
import com.cedrus.pingpong.model.PingPongMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class PingPongProducer {
    private final KafkaConfig kafkaConfig;
    private final ObjectMapper objectMapper;
    private final Producer<String, String> producer;

    @Autowired public PingPongProducer(KafkaConfig kafkaConfig, ObjectMapper objectMapper) {
        this.kafkaConfig = kafkaConfig;
        this.objectMapper = objectMapper;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(props);
    }

    public void sendMessage(PingPongMessage message) throws JsonProcessingException {
        log.info("==== SENDING MESSAGE ON {} ====", message.getTopic());

        producer.send(new ProducerRecord<>(message.getTopic(), null, objectMapper.writeValueAsString(message)));
    }
}
