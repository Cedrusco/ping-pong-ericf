package com.cedrus.pingpong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PingPongApplication {
    public static void main(String[] args) {
        SpringApplication.run(PingPongApplication.class, args);
    }
}