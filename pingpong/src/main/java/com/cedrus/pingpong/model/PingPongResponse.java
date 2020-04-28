package com.cedrus.pingpong.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PingPongResponse {
    @JsonProperty private boolean success;
    @JsonProperty private String responseText;
}
