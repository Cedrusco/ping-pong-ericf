package com.cedrus.pingpong;

import com.cedrus.pingpong.service.PongPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class PingPongPlayerTwo {
    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerTwo.class, args);
    }

    @Bean
    public CommandLineRunner startPlayerTwo(ApplicationContext ctx) {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Pong~~~~~~~~~~~~~~~~~~~~~~~~");
        return args -> {
            ((PongPlayerService) ctx.getBean("pongPlayerService")).startPlaying();
        };
    }
}