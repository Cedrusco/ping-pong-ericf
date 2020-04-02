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

        String topic = pingPongRequest.getTopic();
        if (pingPongRequest.getTopic().toUpperCase().equals("PING")) topic = topicConfig.getPing();
        if (pingPongRequest.getTopic().toUpperCase().equals("PONG")) topic = topicConfig.getPong();

        try {
            if (pingPongRequest.getColor() == null) throw new Exception("Must include color!");
            addBall(topic, pingPongRequest.getColor());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResponseText(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setSuccess(true);
        response.setResponseText("Started Game!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void addBall(String topic, String color) throws JsonProcessingException {
        startGameService.startGame(new PingPongMessage(topic, "1", color));
    }
}
