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
    private KafkaConfig kafkaConfig;
    @Autowired public PingPongProducer(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public void sendMessage(PingPongMessage message) throws JsonProcessingException {
        log.info("==== SENDING MESSAGE ====");
        log.info("==== " + message.getTopic() + " ====");
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<>(message.getTopic(), null, new ObjectMapper().writeValueAsString(message)));
        producer.close();
    }
}
