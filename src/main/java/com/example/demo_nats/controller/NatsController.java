package com.example.demo_nats.controller;

import com.example.demo_nats.nats.Consumer;
import com.example.demo_nats.nats.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/nats")
public class  NatsController {
  private final Producer producer;
  private final Consumer consumer;

  @PostMapping("/send")
  public void sendMessage(@RequestParam String message) throws Exception {
    producer.sendMessage(message);
  }

  @PostMapping
  public void re(){
    consumer.subscribe();
  }

}
