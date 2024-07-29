package com.example.demo_nats.nats;

import io.nats.client.Connection;
import io.nats.client.JetStream;
import io.nats.client.JetStreamOptions;
import io.nats.client.api.PublishAck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

  @Value("${job.nats.topic}")
  String topic;

  private final Connection natsConnection;

  public void sendMessage(String message) {
    try {
      log.info("Sending message :{}", message);
      JetStream jetStream = natsConnection.jetStream(JetStreamOptions.DEFAULT_JS_OPTIONS);
      PublishAck ack = jetStream.publish(topic, message.getBytes());
      log.info("da gui " + message);
      if (ack.hasError()) {
        log.error("Error publishing message: {}", ack.getError());
      } else {
        log.info("Message successfully published to {}, sequence: {}", topic, ack.getSeqno());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
