package com.cedrus.pingpong.service;

import com.cedrus.pingpong.kafka.PingPongProducer;
import com.cedrus.pingpong.model.PingPongMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StartGameService {
    private PingPongProducer producer;

    @Autowired
    public StartGameService(PingPongProducer producer) {
        this.producer = producer;
    }

    public void startGame(PingPongMessage message) throws JsonProcessingException {
        producer.sendMessage(message);
    }
}
