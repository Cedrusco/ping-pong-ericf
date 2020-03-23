package com.pingpong.pingpong;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class PingPongPlayerOne {

    public static void main(String[] args) {
        SpringApplication.run(PingPongPlayerOne.class, args);

        Producer producer = createProducer();
        producer.initTransactions();

        for (int i = 0; i < 10; i++) {
            producer.beginTransaction();
            producer.send(new ProducerRecord<>("ping", Integer.toString(i), Integer.toString(i)));
            System.out.println("Ping" + " " + Integer.toString(i));
            producer.commitTransaction();
        }
    }

    public static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("transactional.id", "ping-pong");

        return new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
    }

}