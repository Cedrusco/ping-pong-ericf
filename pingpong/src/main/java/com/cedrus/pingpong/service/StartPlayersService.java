package com.cedrus.pingpong.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartPlayersService {
    private TeamAService teamAService;
    private TeamBService teamBService;

    @Autowired
    public StartPlayersService(TeamAService teamAService, TeamBService teamBService) {
        this.teamAService = teamAService;
        this.teamBService = teamBService;
    }

    @Bean
    public void startTeams() {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Ping Pong~~~~~~~~~~~~~~~~~~~~~~~~");
        teamAService.run();
        teamBService.run();
    }
}
