package com.cedrus.pingpong;

import com.cedrus.pingpong.service.TeamAService;
import com.cedrus.pingpong.service.TeamBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class PingPongApplication {
    private TeamAService teamAService;
    private TeamBService teamBService;

    public static void main(String[] args) {
        SpringApplication.run(PingPongApplication.class, args);
    }

    @Autowired
    public PingPongApplication(TeamAService teamAService, TeamBService teamBService) {
        this.teamAService = teamAService;
        this.teamBService = teamBService;
    }

    @Bean
    public CommandLineRunner startTeams(ApplicationContext context) {
        try {
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~Starting Ping Pong~~~~~~~~~~~~~~~~~~~~~~~~");
            return args -> {
                ((TeamAService) context.getBean("teamAService")).run();
                ((TeamBService) context.getBean("teamBService")).run();
            };
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}