package com.pingpong.pingpong;

import com.pingpong.pingpong.config.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Properties;

@SpringBootApplication
public class PingPongPlayerOne {
    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerOne.class, args);
    }
}