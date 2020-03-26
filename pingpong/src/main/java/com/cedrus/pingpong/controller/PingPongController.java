package com.cedrus.pingpong.controller;

import com.cedrus.pingpong.kafka.PingPongProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    @Autowired private PingPongProducer producer;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public String startPing() {
        addBall("ping");
        return "Started Ping!";
    }

    @RequestMapping(value = "/pong", method = RequestMethod.POST)
    @ResponseBody
    public String startPong() {
        addBall("pong");
        return "Started Pong!";
    }

    private void addBall(String ballType) {
        producer.sendMessage(ballType, "1");
    }
}
