package com.cedrus.pingpong.service;

import com.cedrus.pingpong.config.KafkaConfig;
import com.cedrus.pingpong.kafka.PingPongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class TeamBService implements Runnable {
    KafkaConfig kafkaConfig;
    PingPongStream teamBPlayerOne;
    PingPongStream teamBPlayerTwo;

    @Autowired
    public TeamBService(KafkaConfig kafkaConfig, PingPongStream teamBPlayerOne, PingPongStream teamBPlayerTwo) {
        this.kafkaConfig = kafkaConfig;
        this.teamBPlayerOne = teamBPlayerOne;
        this.teamBPlayerTwo = teamBPlayerTwo;
    }

    public void run() {
        try {
            log.info(kafkaConfig.getTeamAGroupId());
            teamBPlayerTwo.startStream(kafkaConfig.getTeamBGroupId(), "B1");
            teamBPlayerTwo.startStream(kafkaConfig.getTeamBGroupId(), "B2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}