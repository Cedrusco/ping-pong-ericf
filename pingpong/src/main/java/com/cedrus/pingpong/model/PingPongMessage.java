package com.cedrus.pingpong.model;

import lombok.Data;

@Data
public class PingPongMessage {
    private String topic;
    private String message;

    public PingPongMessage(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
}
