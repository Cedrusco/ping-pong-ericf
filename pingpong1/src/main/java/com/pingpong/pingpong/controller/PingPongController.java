package com.pingpong.pingpong.controller;

import com.pingpong.pingpong.kafka.PingPongProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    @Autowired private PingPongProducer producer;

    @RequestMapping(value = "/ball", method = RequestMethod.POST)
    @ResponseBody
    public String startBall() {
        addBall("ping");
        return "Started!";
    }

    private void addBall(String ballType) {
        producer.sendMessage(ballType, "Ball");
    }
}
