package com.cedrus.pingpong;

import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.service.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PingPongPlayerOne {
    @Autowired
    TopicConfig topicConfig;
    @Autowired
    private Player playerOne;
    @Autowired
    private Player playerTwo;

    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerOne.class, args);
    }

    @Bean(name="playerOne")
    public CommandLineRunner startPlayerOne(ApplicationContext ctx) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~" + "Starting Ping" + "~~~~~~~~~~~~~~~~~~~~~~~~");
        return args -> {
            ((Player) ctx.getBean("player")).startPlaying(topicConfig.getPing());
        };
    }

    @Bean(name="playerTwo")
    public CommandLineRunner startPlayerTwo(ApplicationContext ctx) {
        return args -> {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~" + "Starting Pong" + "~~~~~~~~~~~~~~~~~~~~~~~~");
            ((Player) ctx.getBean("player")).startPlaying(topicConfig.getPong());
        };
    }
}