package com.example.demo_nats.nats;

import io.nats.client.*;
import io.nats.client.api.ConsumerConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.nats.client.JetStream;



import java.nio.charset.StandardCharsets;
import java.time.Duration;


@Service
@Slf4j
@RequiredArgsConstructor
public class Consumer {

  private final JetStream jetStream;
  @Value("${job.nats.topic}")
  String topic;

  @PostConstruct
  public void subscribe() {
    try {

      ConsumerConfiguration consumerConfig = ConsumerConfiguration.builder()
            .ackPolicy(ConsumerConfiguration.DEFAULT_ACK_POLICY)
            .build();

      PushSubscribeOptions options = PushSubscribeOptions.builder()
            .stream("example-stream")
            .configuration(consumerConfig)// Thay "YOUR_STREAM_NAME" bằng tên stream của bạn
            .build();

      // Đăng ký nhận tin nhắn
      Subscription subscription = jetStream.subscribe(topic, options);

      // Xử lý tin nhắn
      new Thread(() -> {
        while (true) {
          Message msg = null;
          try {
            msg = subscription.nextMessage(Duration.ofSeconds(5));
            if (msg != null) {
              String receivedMessage = new String(msg.getData(), StandardCharsets.UTF_8);
              log.info("Received message: {}", receivedMessage);
              msg.ack();
            }
          } catch (Exception e) {
            log.error("Error processing message", e);
            if (msg != null) {
              msg.nak();
            }
          }
        }
      }, "NATS JetStream Consumer").start();

    } catch (Exception e) {
      log.error("Error setting up JetStream subscription", e);
    }
  }
}
