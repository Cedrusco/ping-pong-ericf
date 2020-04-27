package com.cedrus.pingpong.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PingPongRequest {
    @JsonProperty
    private String color;
}
