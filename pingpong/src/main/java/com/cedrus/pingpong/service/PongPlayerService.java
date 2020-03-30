package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.AppConfig;
import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.kafka.PingPongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class PongPlayerService implements Runnable {
    @Autowired
    AppConfig appConfig;
    @Autowired
    TopicConfig topicConfig;
    @Autowired
    PingPongStream pongStream;

    public void run() {
        try {
            pongStream.startListening(topicConfig.getPong());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
