package com.example.demo_nats.nats;

import io.nats.client.JetStream;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

  @Value("${job.nats.topic}")
  String topic;
  private final JetStream jetStream;

  public void sendMessage(String message) throws Exception {
    log.info("Sending message: {}", message);
    // Tạo một tin nhắn
    Message msg = NatsMessage.builder()
          .subject(topic)
          .data(message.getBytes())
          .build();

    // Gửi tin nhắn và trả về xác nhận
    jetStream.publish(msg);
  }

}
