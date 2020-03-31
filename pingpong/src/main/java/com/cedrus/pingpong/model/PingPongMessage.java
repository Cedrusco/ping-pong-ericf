package com.cedrus.pingpong.model;

import lombok.Data;

@Data
public class PingPongMessage {
    private String topic;
    private String count;
    private String color;

    public PingPongMessage(String topic, String count, String color) {
        this.topic = topic;
        this.count = count;
        this.color = color;
    }
    public PingPongMessage(){}
}
