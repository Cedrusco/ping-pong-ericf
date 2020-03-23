package com.pingpong.pingpong;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
public class PingPongPlayerTwo {

	public static void main(String[] args) {
		SpringApplication.run(PingPongPlayerTwo.class, args);

		final int giveUp = 500;
		int noRecordsCount = 0;

		KafkaConsumer consumer = createConsumer();

		while (true) {
			final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
			if (consumerRecords.count() == 0) {
				noRecordsCount++;
				if (noRecordsCount > giveUp) break;
				else continue;
			}
			for (ConsumerRecord<String, String> record : consumerRecords) {
				System.out.println("Got Ping" + " " + record.value());
			}

			consumer.commitAsync();
		}
		consumer.close();
	}

	public static KafkaConsumer<String, String> createConsumer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("transactional.id", "ping-pong");
		props.put("group.id", "test");

		KafkaConsumer consumer = new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
		consumer.subscribe(Collections.singletonList("ping"));
		return consumer;
	}
}