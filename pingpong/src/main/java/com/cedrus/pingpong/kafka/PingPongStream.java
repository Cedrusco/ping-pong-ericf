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
import java.util.UUID;


@Slf4j
@Component
public class PingPongStream {
    private KafkaConfig kafkaConfig;
    private TopicConfig topicConfig;
    private AppConfig appConfig;
    private ObjectMapper objectMapper;

    @Autowired PingPongStream(KafkaConfig kafkaConfig,
                              TopicConfig topicConfig,
                              AppConfig appConfig,
                              ObjectMapper objectMapper)
    {
        this.kafkaConfig = kafkaConfig;
        this.topicConfig = topicConfig;
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
    }

    public void startStream(String teamGroupId, String playerId) throws IOException {

        log.info("==== STARTED STREAM ON " + topicConfig.getPingPong() + " ====");
        Serde<String> stringSerde = Serdes.String();

        buildStream(teamGroupId, stringSerde, playerId);
    }

    private void transformAndProduceStream(KStream stream, Serde serde, String playerId) {
        KStream<String, String> mappedValuesStream = stream.transformValues(createMessageProcessor(playerId));
        mappedValuesStream.to(topicConfig.getPingPong(), Produced.with(serde, serde));
    }

    private void buildStream(String teamGroupId, Serde serde, String playerId) {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, teamGroupId);

        StreamsBuilder builder = new StreamsBuilder();

        KStream stream = builder.stream(topicConfig.getPingPong(), Consumed.with(serde, serde));
        stream.selectKey(new KeyValueMapper() {
            @Override
            public Object apply(Object key, Object value) {
                UUID random = UUID.randomUUID();
                return random;
            }
        });
        KStream[] branches = stream.branch((key, value) -> {
            PingPongMessage message = new PingPongMessage();
            try {
                message = objectMapper.readValue(value.toString(), PingPongMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("This player: " + playerId + "... Incoming ball's player: " + message.getPlayerId());
            //Check that this ball has not been hit most recently by own team
            String ballPlayerId = message.getPlayerId();
            return !ballPlayerId.contains("" + playerId.charAt(0));
        });
        transformAndProduceStream(branches[0], serde, playerId);
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    private ValueTransformerSupplier<String, String> createMessageProcessor(String playerId) {
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

                log.info("=======" + message.getTopic() + " " + message.getColor() + " " + message.getCount() + " " + playerId + " hitting " + "=======");
                message.setCount(Integer.toString(Integer.parseInt(message.getCount()) + 1));
                message.setPlayerId(playerId);

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