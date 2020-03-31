package com.cedrus.pingpong.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartPlayersService {
    private PingPlayerService pingPlayerService;
    private PongPlayerService pongPlayerService;

    @Autowired
    public StartPlayersService(PingPlayerService pingPlayerService, PongPlayerService pongPlayerService) {
        this.pingPlayerService = pingPlayerService;
        this.pongPlayerService = pongPlayerService;
    }

    @Bean
    public void startPlayers() {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Ping Pong~~~~~~~~~~~~~~~~~~~~~~~~");
        pingPlayerService.run();
        pongPlayerService.run();
    }
}
