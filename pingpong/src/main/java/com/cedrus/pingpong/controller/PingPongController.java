package com.cedrus.pingpong.controller;

import com.cedrus.pingpong.kafka.PingPongProducer;
import com.cedrus.pingpong.model.PingPongMessage;
import com.cedrus.pingpong.model.PingPongRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PingPongController {
    @Autowired private PingPongProducer producer;

    @PostMapping(value = "/ball")
    @ResponseBody
    public String startPing(@RequestBody PingPongRequest pingPongRequest) throws JsonProcessingException {
        addBall(pingPongRequest.getTopic(), pingPongRequest.getColor());
        return "Started Ping!";
    }

    private void addBall(String topic, String color) throws JsonProcessingException {
        producer.sendMessage(new PingPongMessage(topic, "1", color));
    }
}
