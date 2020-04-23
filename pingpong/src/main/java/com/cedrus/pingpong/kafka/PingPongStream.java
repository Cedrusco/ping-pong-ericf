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
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;


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
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StreamsPartitionAssignor.class.getName());

        StreamsBuilder builder = new StreamsBuilder();

        KStream stream = builder.stream(topicConfig.getPingPong(), Consumed.with(serde, serde));
        stream.selectKey(new KeyValueMapper() {
            @Override
            public Object apply(Object key, Object value) {
                return generateRandomString();
            }
        });
        transformAndProduceStream(stream, serde, playerId);
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

                log.info("=======" + message.getTopic() + " " + message.getColor() + " " + message.getCount() + " " + playerId + "=======");
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

    private String generateRandomString() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}