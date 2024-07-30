package com.example.demo_nats.config;

import io.nats.client.*;
import io.nats.client.api.RetentionPolicy;
import io.nats.client.api.StorageType;
import io.nats.client.api.StreamConfiguration;
import io.nats.client.api.StreamInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class NATSConfig {

  @Value("${job.nats.uri}")
  private String natsUri;

  @Bean
  public Connection getNATSConnection() throws Exception{
    Options options = Options.builder()
          .server(natsUri)
          .build();
    return Nats.connect(options);
  }

  @Bean
  public JetStream jetStream(Connection connection) throws IOException {
    return connection.jetStream();
  }

  @Bean
  public JetStreamManagement jetStreamManagement(Connection connection) {
    try {
      return connection.jetStreamManagement();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Cấu hình và tạo Stream
  @Bean
  public StreamInfo createStream(JetStreamManagement jsm) throws Exception {
    StreamConfiguration config = StreamConfiguration.builder()
          .name("example-stream")
          .subjects("MY_MESSAGE_TOPIC")
          .retentionPolicy(RetentionPolicy.WorkQueue)
          .maxConsumers(10)
          .maxBytes(1_000_000_000)
          .storageType(StorageType.File)
          .replicas(1)
          .build();

    return jsm.addStream(config);
  }
}
