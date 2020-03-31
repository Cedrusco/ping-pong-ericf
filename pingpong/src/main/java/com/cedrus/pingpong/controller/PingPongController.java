package com.cedrus.pingpong.controller;

import com.cedrus.pingpong.config.TopicConfig;
import com.cedrus.pingpong.model.PingPongMessage;
import com.cedrus.pingpong.model.PingPongRequest;
import com.cedrus.pingpong.model.PingPongResponse;
import com.cedrus.pingpong.service.StartGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    private StartGameService startGameService;
    private TopicConfig topicConfig;
    @Autowired
    public PingPongController(StartGameService startGameService, TopicConfig topicConfig) {
        this.startGameService = startGameService;
        this.topicConfig = topicConfig;
    }

    @PostMapping(value = "/ball")
    public ResponseEntity<PingPongResponse> startPing(@RequestBody PingPongRequest pingPongRequest) {
        PingPongResponse response = new PingPongResponse();

        if (!pingPongRequest.getTopic().toUpperCase().equals("PING")
                && !pingPongRequest.getTopic().toUpperCase().equals("PONG")) {
            response.setSuccess(false);
            response.setResponseText("Invalid topic name, please choose either Ping or Pong");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String topic = "";
        if (pingPongRequest.getTopic().toUpperCase().equals("PING")) topic = topicConfig.getPing();
        if (pingPongRequest.getTopic().toUpperCase().equals("PONG")) topic = topicConfig.getPong();

        try {
            addBall(topic, pingPongRequest.getColor());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResponseText(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void addBall(String topic, String color) throws JsonProcessingException {
        startGameService.startGame(new PingPongMessage(topic, "1", color));
    }
}
