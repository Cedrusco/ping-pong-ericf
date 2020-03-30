package com.cedrus.pingpong.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartPlayersService {
    @Autowired PingPlayerService pingPlayerService;
    @Autowired PongPlayerService pongPlayerService;

    @Bean
    public void startPlayers() {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Ping Pong~~~~~~~~~~~~~~~~~~~~~~~~");
        pingPlayerService.run();
        pongPlayerService.run();
    }
}
