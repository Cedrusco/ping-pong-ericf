package com.cedrus.pingpong.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class PingPongMessage {
    @Autowired private String topic;
    @Autowired private String count;
    @Autowired private String color;

    public PingPongMessage(String topic, String count, String color) {
        this.topic = topic;
        this.count = count;
        this.color = color;
    }
    public PingPongMessage(){}
}
