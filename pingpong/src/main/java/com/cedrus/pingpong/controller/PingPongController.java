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

    @RequestMapping(value = "/ball", method = RequestMethod.POST)
    @ResponseBody
    public String startPing(@RequestBody PingPongRequest pingPongRequest) throws JsonProcessingException {
        addBall(pingPongRequest.getTopic(), pingPongRequest.getColor());
        return "Started Ping!";
    }

//    @RequestMapping(value = "/pong", method = RequestMethod.POST)
//    @ResponseBody
//    public String startPong() {
//        addBall("pong");
//        return "Started Pong!";
//    }

    private void addBall(String topic, String color) throws JsonProcessingException {
        producer.sendMessage(new PingPongMessage(topic, "1", color));
    }
}
