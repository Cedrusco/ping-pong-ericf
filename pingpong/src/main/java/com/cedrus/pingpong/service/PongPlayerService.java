package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.kafka.PingPongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class PongPlayerService implements Runnable {
    TopicConfig topicConfig;
    PingPongStream pongStream;

    @Autowired
    public PongPlayerService(TopicConfig topicConfig, PingPongStream pongStream) {
        this.topicConfig = topicConfig;
        this.pongStream = pongStream;
    }

    public void run() {
        try {
            pongStream.startStream(topicConfig.getPong());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
