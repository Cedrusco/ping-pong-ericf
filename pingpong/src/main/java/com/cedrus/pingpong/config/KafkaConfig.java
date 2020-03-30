package com.cedrus.pingpong.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {
    private String bootstrapServers;
    private String groupId;
    private String kafkaAppId;
}
