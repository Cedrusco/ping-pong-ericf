package com.cedrus.pingpong.kafka;

import com.cedrus.pingpong.config.KafkaConfig;
import com.cedrus.pingpong.model.PingPongMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


@Slf4j
@Component
public class PingPongConsumer {
    @Autowired
    private KafkaConfig kafkaConfig;

    public void startListening(String topic, java.util.function.Consumer<PingPongMessage> handler) {
        log.info("==== STARTED LISTENING ON " + topic + " ====");
        Consumer consumer = createConsumer(topic);
        final int giveUp = 500;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            for (ConsumerRecord<String, String> record : consumerRecords) {
                log.info("==== GOT MESSAGE ====");
                log.info("==== " + record.topic() + " " + record.value() + " ====");
                PingPongMessage newMessage = new PingPongMessage(record.topic(), record.value());
                handler.accept(newMessage);
            }
            consumer.commitAsync();
        }
        consumer.close();
    }

    public KafkaConsumer<String, String> createConsumer(String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId());

        KafkaConsumer consumer = new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}
