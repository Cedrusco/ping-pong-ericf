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
    private ObjectMapper objectMapper;

    @Autowired PingPongStream(KafkaConfig kafkaConfig, TopicConfig topicConfig, AppConfig appConfig, ObjectMapper objectMapper) {
        this.kafkaConfig = kafkaConfig;
        this.topicConfig = topicConfig;
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
    }

    public void startStream(String topic) throws IOException {
        log.info("==== STARTED STREAM ON " + topic + " ====");
        Serde<String> stringSerde = Serdes.String();

        buildStream(topic, stringSerde);
    }

    private void transformAndProduceStream(String topic, KStream stream, Serde serde) {
        KStream<String, String> mappedValuesStream = stream.transformValues(createMessageProcessor());
        mappedValuesStream.to(topic, Produced.with(serde, serde));
    }

    private void buildStream(String topic, Serde serde) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConfig.getKafkaAppId() + topic);
        StreamsBuilder builder = new StreamsBuilder();

        KStream stream = builder.stream(topic, Consumed.with(serde, serde));
        transformAndProduceStream(topic, stream, serde);
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    private ValueTransformerSupplier<String, String> createMessageProcessor() {
        return () -> new ValueTransformer<String, String>() {
            @Override
            public void init(ProcessorContext context) {
            }

            @Override
            public String transform(String messageAsString) {
                PingPongMessage message = new PingPongMessage();
                try {
                    message = objectMapper.readValue(messageAsString, PingPongMessage.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log.info("=======" + message.getTopic() + " " + message.getColor() + " " + message.getCount() + "=======");
                message.setCount(Integer.toString(Integer.parseInt(message.getCount()) + 1));

                int minDelaySec = appConfig.getMinDelaySeconds();
                int maxDelaySec = appConfig.getMaxDelaySeconds();
                int deltaDelaySec = maxDelaySec - minDelaySec;
                Random random = new Random();
                int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;
                try {
                    Thread.sleep(sleepTime * 1000L);
                    return objectMapper.writeValueAsString(message);
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