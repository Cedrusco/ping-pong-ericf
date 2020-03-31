package com.cedrus.pingpong.controller;

import com.cedrus.pingpong.model.PingPongMessage;
import com.cedrus.pingpong.model.PingPongRequest;
import com.cedrus.pingpong.service.StartGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    private StartGameService startGameService;
    @Autowired
    public PingPongController(StartGameService startGameService) {
        this.startGameService = startGameService;
    }

    @PostMapping(value = "/ball")
    @ResponseBody
    public String startPing(@RequestBody PingPongRequest pingPongRequest) throws JsonProcessingException {
        addBall(pingPongRequest.getTopic(), pingPongRequest.getColor());
        return "Started Ping!";
    }

    private void addBall(String topic, String color) throws JsonProcessingException {
        startGameService.startGame(new PingPongMessage(topic, "1", color));
    }
}
