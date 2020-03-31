package com.cedrus.pingpong.kafka;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.config.KafkaConfig;
import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.model.PingPongMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;


@Slf4j
@Component
public class PingPongStream {
    private KafkaConfig kafkaConfig;
    private TopicConfig topicConfig;
    private AppConfig appConfig;

    @Autowired PingPongStream(KafkaConfig kafkaConfig, TopicConfig topicConfig, AppConfig appConfig) {
        this.kafkaConfig = kafkaConfig;
        this.topicConfig = topicConfig;
        this.appConfig = appConfig;
    }

    public void startListening(String topic) throws IOException {
        log.info("==== STARTED LISTENING ON " + topic + " ====");
        createStream(topic);
    }

    public void createStream(String topic) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConfig.getKafkaAppId() + topic);

        StreamsBuilder builder = new StreamsBuilder();
        Serde<String> stringSerde = Serdes.String();
        KStream stream = builder.stream(topic, Consumed.with(stringSerde, stringSerde));
        String newTopic = topic.equals(topicConfig.getPing()) ? topicConfig.getPong() : topicConfig.getPing();
        KStream<String, String> mappedValues = stream.transformValues(processMessage());
        mappedValues.to(newTopic, Produced.with(stringSerde, stringSerde));
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    private ValueTransformerSupplier<String, String> processMessage() {
        return () -> new ValueTransformer<String, String>() {
            @Override
            public void init(ProcessorContext context) {
            }

            @Override
            public String transform(String messageAsString) {
                PingPongMessage message = new PingPongMessage();
                try {
                    message = new ObjectMapper().readValue(messageAsString, PingPongMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log.info("=======" + message.getTopic() + " " + message.getColor() + " " + message.getCount() + "=======");
                String newTopic = message.getTopic().equals(topicConfig.getPing()) ? topicConfig.getPong() : topicConfig.getPing();
                message.setTopic(newTopic);
                message.setCount(Integer.toString(Integer.parseInt(message.getCount()) + 1));

                int minDelaySec = appConfig.getMinDelaySeconds();
                int maxDelaySec = appConfig.getMaxDelaySeconds();
                int deltaDelaySec = maxDelaySec - minDelaySec;
                Random random = new Random();
                int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;
                try {
                    Thread.sleep(sleepTime * 1000L);
                    return new ObjectMapper().writeValueAsString(message);
                } catch (InterruptedException | JsonProcessingException e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            public void close() {
            }
        };
    }
}