package com.cedrus.pingpong.model;

import lombok.Data;

@Data
public class PingPongMessage {
    private String topic;
    private String count;
    private String color;
    private String playerId;

    public PingPongMessage(String topic, String count, String color, String playerId) {
        this.topic = topic;
        this.count = count;
        this.color = color;
        this.playerId = playerId;
    }
    public PingPongMessage(){}
}
