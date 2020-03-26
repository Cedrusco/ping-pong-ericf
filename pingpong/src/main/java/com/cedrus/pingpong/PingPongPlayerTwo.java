package com.cedrus.pingpong;

import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.service.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class PingPongPlayerTwo {
    @Autowired
    private TopicConfig topicConfig;

    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerTwo.class, args);
    }

    @Bean
    public CommandLineRunner startPlayerTwo(ApplicationContext ctx) {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Pong~~~~~~~~~~~~~~~~~~~~~~~~");
        return args -> {
            ((Player) ctx.getBean("player")).startPlaying(topicConfig.getPong());
        };
    }
}