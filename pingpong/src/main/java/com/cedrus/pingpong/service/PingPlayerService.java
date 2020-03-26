package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.kafka.PingPongConsumer;
import com.cedrus.pingpong.kafka.PingPongProducer;
import com.cedrus.pingpong.model.PingPongMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Consumer;

@Slf4j
@Service
public class PingPlayerService implements Runnable {
    @Autowired
    AppConfig appConfig;
    @Autowired
    TopicConfig topicConfig;
    @Autowired
    PingPongProducer producer;
    @Autowired
    PingPongConsumer consumer;

    public void run() {
        consumer.startListening(topicConfig.getPing(), respond);
    }

    Consumer<PingPongMessage> respond = pingPongMessage -> {
        int minDelaySec = appConfig.getMinDelaySeconds();
        int maxDelaySec = appConfig.getMaxDelaySeconds();
        int deltaDelaySec = maxDelaySec - minDelaySec;
        Random random = new Random();
        int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;

        String newTopic = pingPongMessage.getTopic().equals(topicConfig.getPing()) ? topicConfig.getPong() : topicConfig.getPing();
        try {
            Thread.sleep(sleepTime * 1000L);
            producer.sendMessage(newTopic, Integer.toString(Integer.parseInt(pingPongMessage.getMessage())+1));
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
    };
}
