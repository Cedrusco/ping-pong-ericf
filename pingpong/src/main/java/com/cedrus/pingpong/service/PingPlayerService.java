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
public class PingPlayerService implements Runnable {
    @Autowired
    AppConfig appConfig;
    @Autowired
    TopicConfig topicConfig;
    @Autowired
    PingPongStream pingStream;

    public void run() {
        try {
            pingStream.startListening(topicConfig.getPing());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
