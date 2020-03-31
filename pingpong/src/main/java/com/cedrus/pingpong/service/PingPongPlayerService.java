package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.kafka.PingPongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
//Service for One-Topic Solution
public class PingPongPlayerService implements Runnable {
    TopicConfig topicConfig;
    PingPongStream playerOne;
    PingPongStream playerTwo;

    @Autowired
    public PingPongPlayerService(TopicConfig topicConfig, PingPongStream playerOne, PingPongStream playerTwo) {
        this.topicConfig = topicConfig;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void run() {
        try {
            playerOne.startStream(topicConfig.getPingPong());
            playerTwo.startStream(topicConfig.getPingPong());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
