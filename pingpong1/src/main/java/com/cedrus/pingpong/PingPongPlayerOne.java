package com.cedrus.pingpong;

import com.cedrus.pingpong.service.PlayerOne;
import com.cedrus.pingpong.service.PlayerTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PingPongPlayerOne {
    @Autowired private PlayerOne playerOne;
    @Autowired private PlayerTwo playerTwo;

    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerOne.class, args);
    }

    @Bean
    public CommandLineRunner startPlayerOne(ApplicationContext ctx) {
        return args -> {
            ((PlayerOne) ctx.getBean("playerOne")).startPlaying();
        };
    }

    @Bean
    public CommandLineRunner startPlayerTwo(ApplicationContext ctx) {
        return args -> {
            ((PlayerOne) ctx.getBean("playerTwo")).startPlaying();
        };
    }
}