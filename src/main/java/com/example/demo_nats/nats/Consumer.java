package com.example.demo_nats.nats;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class Consumer {

  private final Connection natsConnection;
  @Value("${job.nats.topic}")
  String topic;

  @PostConstruct
  public void subscribe() {

    Dispatcher dispatcher = natsConnection.createDispatcher();
    dispatcher.subscribe(topic, message -> {
      try {
        log.info("da toi day");
        log.info("Received message: {}", new String(message.getData()));
        String mgs = new String(message.getData());
        System.out.println("Received mgs: {}"+ mgs);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
