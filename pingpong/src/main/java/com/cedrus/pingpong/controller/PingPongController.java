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
    private final StartGameService startGameService;
    private final TopicConfig topicConfig;
    @Autowired
    public PingPongController(StartGameService startGameService, TopicConfig topicConfig) {
        this.startGameService = startGameService;
        this.topicConfig = topicConfig;
    }

    @PostMapping(value = "/ball")
    public ResponseEntity<PingPongResponse> startPing(@RequestBody PingPongRequest pingPongRequest) {
        final PingPongResponse response = new PingPongResponse();

        try {
            if (pingPongRequest.getColor() == null) throw new Exception("Must include color!");
            addBall(pingPongRequest.getColor());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResponseText(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setSuccess(true);
        response.setResponseText("Started Game!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void addBall(String color) throws JsonProcessingException {
        startGameService.startGame(new PingPongMessage(topicConfig.getPingPong(), "1", color, "A1")); //Must have a player "serve"
    }
}
