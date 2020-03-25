package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.kafka.PingPongConsumer;
import com.cedrus.pingpong.kafka.PingPongProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Function;

//Listens for "pongs", sends "pings"
@Slf4j
@Service
public class PlayerTwo {
    @Autowired
    AppConfig appConfig;
    @Autowired
    PingPongProducer producer;
    @Autowired
    PingPongConsumer consumer;

    public void startPlaying() {
        consumer.startListening("pong", respond);
    }

    Function<String, String> respond = (message) -> {
        int minDelaySec = appConfig.getMinDelaySeconds();
        int maxDelaySec = appConfig.getMaxDelaySeconds();
        int deltaDelaySec = maxDelaySec - minDelaySec;
        Random random = new Random();
        int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;
        try {
            Thread.sleep(sleepTime * 1000L);
            producer.sendMessage(message, "Hello2!!!");
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    };
}
