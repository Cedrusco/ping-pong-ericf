package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.KafkaConfig;
import com.cedrus.pingpong.kafka.PingPongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TeamAService implements Runnable {
    KafkaConfig kafkaConfig;
    PingPongStream teamAPlayerOne;
    PingPongStream teamAPlayerTwo;

    @Autowired
    public TeamAService(KafkaConfig kafkaConfig, PingPongStream teamAPlayerOne, PingPongStream teamAPlayerTwo) {
        this.kafkaConfig = kafkaConfig;
        this.teamAPlayerOne = teamAPlayerOne;
        this.teamAPlayerTwo = teamAPlayerTwo;
    }

    public void run() {
        try {
            teamAPlayerTwo.startStream(kafkaConfig.getTeamAGroupId(), "A1");
            teamAPlayerTwo.startStream(kafkaConfig.getTeamAGroupId(), "A2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
