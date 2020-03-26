package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.kafka.PingPongConsumer;
import com.cedrus.pingpong.kafka.PingPongProducer;
import com.cedrus.pingpong.model.PingPongMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;

@Slf4j
@Service
public class PongPlayerService implements Runnable {
    @Autowired
    AppConfig appConfig;
    @Autowired
    TopicConfig topicConfig;
    @Autowired
    PingPongProducer producer;
    @Autowired
    PingPongConsumer consumer;

    public void run() {
        try {
            consumer.startListening(topicConfig.getPong(), respond);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Consumer<PingPongMessage> respond = message -> {
        int minDelaySec = appConfig.getMinDelaySeconds();
        int maxDelaySec = appConfig.getMaxDelaySeconds();
        int deltaDelaySec = maxDelaySec - minDelaySec;
        Random random = new Random();
        int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;

        String newTopic = message.getTopic().equals(topicConfig.getPing()) ? topicConfig.getPong() : topicConfig.getPing();
        try {
            Thread.sleep(sleepTime * 1000L);
            producer.sendMessage(new PingPongMessage(newTopic, Integer.toString(Integer.parseInt(message.getCount()) + 1), message.getColor()));
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
    };
}
