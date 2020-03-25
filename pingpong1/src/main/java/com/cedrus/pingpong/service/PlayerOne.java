package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.kafka.PingPongConsumer;
import com.cedrus.pingpong.kafka.PingPongProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Function;

//Listens for "pings", sends "pongs"
@Slf4j
@Service
public class PlayerOne {
    @Autowired
    AppConfig appConfig;
    @Autowired
    PingPongProducer producer;
    @Autowired
    PingPongConsumer consumer;

    public void startPlaying() {
        consumer.startListening("ping", respond);
    }

    Function<String, String> respond = (message) -> {
        int minDelaySec = appConfig.getMinDelaySeconds();
        int maxDelaySec = appConfig.getMaxDelaySeconds();
        int deltaDelaySec = maxDelaySec - minDelaySec;
        Random random = new Random();
        int sleepTime = random.nextInt(deltaDelaySec) + minDelaySec;
        try {
            Thread.sleep(sleepTime * 1000L);
            producer.sendMessage(message, "Hello1!!!");
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    };
}
